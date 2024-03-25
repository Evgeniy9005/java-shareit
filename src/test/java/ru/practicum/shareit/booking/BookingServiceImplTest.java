package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.data.Data;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.State;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private List<Booking> bookingList;

    private List<Item> itemList;

    private List<User> userList;

    Comparator<Booking> bookingSortDescById;
    Comparator<Booking> bookingSortDescByStart;

    @BeforeEach
    void start() {


        bookingSortDescById = (b1,b2) -> Math.toIntExact(b2.getId() - b1.getId());

        bookingSortDescByStart = (b1,b2) -> b2.getStart().compareTo(b1.getStart());

          /*      new Comparator<Booking>() {
            @Override
            public int compare(Booking o1, Booking o2) {
                return o2.getStart().compareTo(o1.getStart());
            }
        };*/

        bookingService = new BookingServiceImpl(bookingRepository,userRepository,itemRepository);

        userList = Data.generationData(2,User.class);
        Data.printList(userList,">>>");

        itemList = Data.generationData(2,Item.class);
        Data.printList(itemList,"===");

        bookingList = Data.generationData(8,Booking.class,userList.get(0),itemList.get(0));
        bookingList.sort(bookingSortDescById);
        Data.printList(bookingList,"_+_");
    }

    @Test
    void addBooking() {
    }

    @Test
    void setStatus() {
    }

    @Test
    void getBookingByIdForUserId() {


    }

    @Test
    void getBookingsForUser() {
        long bookerId = 1;

        when(userRepository.existsById(anyLong()))
                .thenReturn(false);

        assertThrows(NotFoundException.class, ()->bookingService.getBookingsForUser(bookerId, Data.ALL,0,10));

        when(userRepository.existsById(anyLong()))
                .thenReturn(true);

        when(bookingRepository.findByBookerIdOrderByIdDesc(anyLong(),any())).thenReturn(bookingList);

        List<Booking> list = (List<Booking>) bookingService.getBookingsForUser(bookerId, Data.ALL,0,10);

        assertNotNull(list);
        assertEquals(8,list.size());
        assertEquals(8,list.get(0).getId());
        assertEquals(7,list.get(1).getId());

        when(bookingRepository.findByBookerIdOrderByStartDesc(anyLong(),any())).thenReturn(bookingList);

        list = (List<Booking>) bookingService.getBookingsForUser(bookerId,Data.FUTURE,0,10);
        list.sort(bookingSortDescByStart);
        assertNotNull(list);
        assertEquals(8,list.size());
        assertEquals(8,list.get(0).getId());
        assertEquals(7,list.get(1).getId());

        List<Booking> listWaiting =  bookingList.stream()
                .map(booking -> booking.toBuilder().status(Status.WAITING).build())
                .collect(Collectors.toList());
        when(bookingRepository.findByBookerIdAndStatusOrderByIdDesc(bookerId,Status.WAITING, PageRequest.of(0,10)))
                .thenReturn(listWaiting);

        list = (List<Booking>) bookingService.getBookingsForUser(bookerId,Data.WAITING,0,10);
        list.sort(bookingSortDescById);
        assertNotNull(list);
        assertEquals(8,list.size());
        assertEquals(8,list.get(0).getId());
        assertEquals(7,list.get(1).getId());
        assertEquals(Status.WAITING,list.get(5).getStatus());

        List<Booking> listRejected =  bookingList.stream()
                .map(booking -> booking.toBuilder().status(Status.REJECTED).build())
                .collect(Collectors.toList());
        when(bookingRepository.findByBookerIdAndStatusOrderByIdDesc(bookerId,Status.REJECTED, PageRequest.of(0,10)))
                .thenReturn(listRejected);

        list = (List<Booking>) bookingService.getBookingsForUser(bookerId,Data.REJECTED,0,10);
        list.sort(bookingSortDescById);
        assertNotNull(list);
        assertEquals(8,list.size());
        assertEquals(8,list.get(0).getId());
        assertEquals(7,list.get(1).getId());
        assertEquals(Status.REJECTED,list.get(5).getStatus());

        when(bookingRepository.findByBookingCurrentForBooker(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(bookingList);
        list = (List<Booking>) bookingService.getBookingsForUser(bookerId,Data.CURRENT,0,10);
        assertNotNull(list);
        assertEquals(8,list.size());

        when(bookingRepository.findByBookerIdAndEndBeforeOrderByEndDesc(anyLong(), any(), any()))
                .thenReturn(bookingList);
        list = (List<Booking>) bookingService.getBookingsForUser(bookerId,Data.PAST,0,10);
        assertNotNull(list);
        assertEquals(8,list.size());

        when(bookingRepository.findByBookerIdAndEndBeforeOrderByEndDesc(anyLong(), any(), any()))
                .thenReturn(bookingList);
        list = (List<Booking>) bookingService.getBookingsForUser(bookerId,Data.PAST,0,10);
        assertNotNull(list);
        assertEquals(8,list.size());

        assertThrows(UnsupportedStatusException.class,
                ()->bookingService.getBookingsForUser(bookerId,Data.UNSUPPORTED_STATUS,0,10));

        when(bookingRepository.findByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(bookingList);

        list = (List<Booking>) bookingService.getBookingsForUser(bookerId,"",0,10);
        assertEquals(8,list.size());



        verify(bookingRepository).findByBookerIdOrderByIdDesc(anyLong(),any());
        verify(bookingRepository,times(2)).findByBookerIdOrderByStartDesc(anyLong(),any());
        verify(bookingRepository).findByBookerIdAndStatusOrderByIdDesc(anyLong(),any(),any());
        verify(bookingRepository).findByBookingCurrentForBooker(anyLong(),any(),any());
        verify(bookingRepository).findByBookerIdAndEndBeforeOrderByEndDesc(anyLong(),any(),any());

        verify(bookingRepository,never()).findAll();

    }

    @Test
    void getBookingsOwnerState() {

    }
}
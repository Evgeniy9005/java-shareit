package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.data.Data;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.util.Util;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

 /*   @Autowired
    private ItemRequestRepository itemRequestRepository;*/

    private List<User> savedUser;

    private List<Item> savedItems;

    private List<Booking> savedBooking;

  // private List<ItemRequest> savedItemRequest;

    @BeforeEach
    void start() {
        savedUser = Data.<User>generationData(2,User.class)
                .stream()
                .map(user -> userRepository.save(user))
                .collect(Collectors.toList());
        Data.printList(savedUser,"***");

     /*   savedItemRequest = Data.<ItemRequest>generationData(2,ItemRequest.class)
                .stream()
                .map(itemRequest -> itemRequestRepository.save(itemRequest))
                .collect(Collectors.toList());
        Data.printList(savedItemRequest,">");*/

        savedItems = Data.<Item>generationData(5,Item.class)
                .stream()
                .map(item -> itemRepository.save(item))
                .collect(Collectors.toList());
        Data.printList(savedItems,"^^^");

        savedBooking = Data.<Booking>generationData(8, Booking.class,savedUser.get(0),savedItems.get(1))
                .stream()
                .map(b -> bookingRepository.save(b))
                .collect(Collectors.toList());
        Data.printList(savedBooking,"===");

    }

    @Test
    void findAllOrderByIdDesc() {

        Sort sortByDate = Sort.by(Sort.Direction.DESC,"start");

        Pageable pageable = Util.validPageParam(0,10,sortByDate);
        List<Booking> bookingList = bookingRepository.findAll(pageable).getContent();
        Data.printList(bookingList,">>>");
        assertEquals(8,bookingList.size());

        pageable = Util.validPageParam(4,2,sortByDate);
        bookingList = bookingRepository.findAll(pageable).getContent();
        Data.printList(bookingList,"---");
        assertEquals(1,bookingList.size());

    }

    @Test
    void findByItemOwnerIdOrderByStartDesc() {

        List<Booking> bookingList = pagination(0,10);
        Data.printList(bookingList,">>>");
        assertEquals(8,bookingList.size());

        bookingList = pagination(3,2); //должен вернуть четвертый элемент по счету
       // Data.printList(bookingList,"+++");
        assertEquals(1,bookingList.size());
        assertEquals(5,bookingList.get(0).getId());

        bookingList = pagination(4,2); //должен вернуть пятый элемент по счету
       // Data.printList(bookingList,"===");
        assertEquals(2,bookingList.size());

        bookingList = pagination(4,2); //должен вернуть пятый элемент по счету
        // Data.printList(bookingList,"===");
        assertEquals(2,bookingList.size());

        bookingList = pagination(0,1); //должен вернуть первый элемент по счету
        // Data.printList(bookingList,"=*=");
        assertEquals(1,bookingList.size());

        bookingList = pagination(0,3); //должен вернуть первый 3 элемента по счету
        // Data.printList(bookingList,"=*=");
        assertEquals(3,bookingList.size());

        bookingList = pagination(4,3); //должен вернуть 5 и 6 элемент по счету
       // Data.printList(bookingList,"=*=");
        assertEquals(2,bookingList.size());
        assertEquals(4,bookingList.get(0).getId());
        assertEquals(3,bookingList.get(1).getId());

        bookingList = pagination(6,3); //должен вернуть 7 и 8 элемент по счету
        // Data.printList(bookingList,"=*=");
        assertEquals(2,bookingList.size());

        bookingList = pagination(7,3); //должен вернуть 8 элемент по счету
       // Data.printList(bookingList,"=*=");
        assertEquals(1,bookingList.size());

        bookingList = pagination(1,1); //должен вернуть 2 элемент по счету
        Data.printList(bookingList,"=*=");
        assertEquals(1,bookingList.size());
        assertEquals(7,bookingList.get(0).getId());
    }

    private List<Booking> pagination(int from, int size) {
        Pageable page = Util.validPageParam(from,size);
        int start = 0;

        if(from > 1) {
            start = from % size;
        }

        System.out.println(
    " $$$$$$$$ start " + start + " $$$$$$$$ from " + from + " $$$$$$$$ size " + size + " $$$$$$$$ page " + page
    );

        /*if(start == 0) {
            return bookingRepository.findByItemOwnerIdOrderByStartDesc(1, page);

        } else {*/
        return bookingRepository.findByItemOwnerIdOrderByStartDesc(1,page).stream()
                    .skip(start)
                    .collect(Collectors.toList());
       // }
    }
}
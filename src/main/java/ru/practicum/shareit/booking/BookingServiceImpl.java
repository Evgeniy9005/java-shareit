package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBooking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final BookingMapper mapper;

    @Override
    public Booking addBooking(CreateBooking createBooking, Long userId) {

    validDate(createBooking);

    Item item = itemRepository.findById(createBooking.getItemId())
            .orElseThrow(()-> new NotFoundException("Не найдена, при бронировании вещь!"));

        if (!item.isAvailable()) {
            throw new BadRequestException("Вещь # уже забронированна!",createBooking.getItemId());
        }

    Booking booking = Booking.builder()
            .start(createBooking.getStart())
            .end(createBooking.getEnd())
            .item(item)
            .booker(userRepository.findById(userId).orElseThrow(
                    ()->new NotFoundException("Не найден, при бронировании пользователь " + userId)))
            .status(Status.WAITING)
            .build();

        Booking newBooking = bookingRepository.save(booking);

        log.info("Создано бронирование {}",newBooking);

        return newBooking;
    }

    @Override
    public Booking setStatus(long bookingId, long userId, Boolean approved) {

        if (approved == null) {
            throw new BadRequestException("Не определен статус одобрения # вещи на бронирование!", approved);
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()->new NotFoundException("Не найднно бронирование под id = " + bookingId));

        long owner = booking.getItem().getOwner().getId();
        long booker = booking.getBooker().getId();

        if(owner == booker) {

        }

        if (owner == userId) { //если userId владелец вещи
            if (approved) { //и approved = true
                booking.setStatus(Status.APPROVED);//подтверждение бронирования влаельцем вещи
            } else { //и approved = false
                booking.setStatus(Status.REJECTED);//отклонение бронирования влаельцем вещи
            }

        }

        if (booker == userId ) { //если userId бронирующий вещь
            if (!approved) { //и approved = false
                booking.setStatus(Status.CANCELED);//подтверждение бронирования влаельцем вещи
            }
        }

        Booking upBooking = bookingRepository.save(booking);

        log.info("Обновлен статус бронирования {}",upBooking);

        return upBooking;
    }

    @Override
    public Booking getBookingByIdForUserId(long bookingId, long userId) {
        if(!userRepository.existsById(userId)) {
            throw new NotFoundException("Не найден пользователь # при запросе брони # вещи", userId,bookingId);
        }

        return bookingRepository.findById(bookingId)
                .orElseThrow(()->new NotFoundException("Не найдено бронирование под id = " + bookingId));
    }


    public Collection<Booking> getBookingsForUser(long userId) {
        if(!userRepository.existsById(userId)) {
            throw new NotFoundException("Не найден пользователь # при запросе всех бронирований вещей", userId);
        }

        List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));

        log.info("Вернулись брони вещей в количестве = {}",bookingList.size());

        return bookingList;
    }


    public Collection<Booking> getBookingsForOwner(long userId) {
        if(!userRepository.existsById(userId)) {
            throw new NotFoundException("Не найден пользователь # при запросе бронирований этим пользователем", userId);
        }

        List<Booking> bookingList = bookingRepository.findByItemOwnerIdOrderByIdDesc(userId);

        log.info("Вернулись брони вещей в количестве {}, запрощенных владельцем {} броней ",bookingList.size(),userId);

        return bookingList;
    }

    public Collection<Booking> getBookingsState(long userId, String state) {

        switch (state) {
            case "ALL":
                return bookingRepository.findByBookerIdOrderByIdDesc(userId);
            case "FUTURE":
                return bookingRepository.findByBookerIdAndStartBefore(userId,LocalDateTime.now());
            case "UNSUPPORTED_STATUS":
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
            default:
                throw new BadRequestException("Не коректный запрос #, пользователь",state, userId);

        }
    }

    private void validDate(CreateBooking createBooking) {
        LocalDateTime start = createBooking.getStart();
        LocalDateTime end = createBooking.getEnd();

        int equals = start.compareTo(end);

        if(equals == 0) {
            throw new BadRequestException(
                    "Время начала # бронирования не может быть равно времени окончания #",start,end
            );
        }

        if(equals > 0) {
            throw new BadRequestException(
                    "Время начала # бронирования не может быть позже времени окончания #",start,end
            );
        }

        if(start.compareTo(LocalDateTime.now()) < 0) {
            throw new BadRequestException("Время начала # бронирования не может быть в прошлом!",start);
        }
    }
}

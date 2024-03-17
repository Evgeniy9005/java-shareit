package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.CreateBooking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

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

    @Override
    @Transactional
    public Booking addBooking(CreateBooking createBooking, Long userId) {

    validDate(createBooking);

    Item item = itemRepository.findById(createBooking.getItemId())
            .orElseThrow(() -> new NotFoundException("Не найдена, при бронировании вещь!"));

    if (item.getOwner().getId() == userId) {
        throw new NotFoundException("Не может владелец вещи создать бронь на свою вещь!");
    }

        if (!item.isAvailable()) {
            throw new BadRequestException("Вещь # уже забронированна!",createBooking.getItemId());
        }

    Booking booking = Booking.builder()
            .start(createBooking.getStart())
            .end(createBooking.getEnd())
            .item(item)
            .booker(userRepository.findById(userId).orElseThrow(
                    () -> new NotFoundException("Не найден, при бронировании пользователь " + userId)))
            .status(Status.WAITING)
            .build();

        Booking newBooking = bookingRepository.save(booking);

        log.info("Создано бронирование {}",newBooking);

        return newBooking;
    }

    @Override
    @Transactional
    public Booking setStatus(long bookingId, long userId, Boolean approved) {

        if (approved == null) {
            throw new BadRequestException("Не определен статус одобрения # вещи на бронирование!");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найднно бронирование под id = " + bookingId));

        Booking setStatusBooking = null;

        long owner = booking.getItem().getOwner().getId();
        long booker = booking.getBooker().getId();

        if (owner == userId) { //если userId владелец вещи
            if (approved) { //и approved = true
                if (booking.getStatus().equals(Status.APPROVED)) {
                    throw new BadRequestException(
                            "Бронирование вещи # уже подтвеждено владельцем #!",booking.getItem().getId(),owner
                    );
                }
               setStatusBooking = booking.toBuilder().status(Status.APPROVED).build();//подтверждение бронирования влаельцем вещи
            } else { //и approved = false
               setStatusBooking = booking.toBuilder().status(Status.REJECTED).build();//отклонение бронирования влаельцем вещи
            }
        }

        if (booker == userId) { //если userId бронирующий вещь
            if (approved) {
                throw new NotFoundException(
                        "Бронирующий # может только отменить бронь!",userId
                );
            } else { //и approved = false
                setStatusBooking = booking.toBuilder().status(Status.CANCELED).build();//отклонение бронирования заказчиком
            }
        }

        log.info("Бронь {} до обновления, статус бронирования {} ",bookingId,setStatusBooking.getStatus());

        Booking upBooking = bookingRepository.save(setStatusBooking);

        log.info("Обновлен статус бронирования {}",upBooking);

        return upBooking;
    }


    @Override
    public Booking getBookingByIdForUserId(long bookingId, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Не найден пользователь # при запросе брони # вещи",userId,bookingId);
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найдено бронирование под id = " + bookingId));


        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotFoundException("Не найден владелец вещи или заказчик # в бронировании # ", userId, booking.getId());
        }

        log.info("Вернулась бронирование {} ",booking);
        return booking;
    }


    @Override
    public Collection<Booking> getBookingsForUser(long userId, String state) { //для ползователя
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Не найден пользователь # при запросе всех бронирований вещей", userId);
        }

        List<Booking> bookingList;
        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
                log.info("Вернулись брони вещей в количестве = {}, c параметром выборки {}", bookingList.size(), state);
                return bookingList;
            case "FUTURE":
                bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "start"));
                log.info("Вернулись брони вещей в количестве = {}, c параметром выборки {}", bookingList.size(), state);
                return bookingList;
            case "WAITING":
                bookingList = bookingRepository.findByBookerIdAndStatusOrderByIdDesc(userId,Status.WAITING);
                log.info("Вернулись брони вещей в количестве = {}, c параметром выборки {}", bookingList.size(), state);
                return bookingList;
            case "REJECTED":
                bookingList = bookingRepository.findByBookerIdAndStatusOrderByIdDesc(userId,Status.REJECTED);
                log.info("Вернулись брони вещей в количестве = {}, c параметром выборки {}", bookingList.size(), state);
                return bookingList;
            case "CURRENT":
                bookingList = bookingRepository.findByBookingCurrentForBooker(userId,LocalDateTime.now());
                log.info("Вернулись брони вещей в количестве = {}, c параметром выборки {}", bookingList.size(), state);
                return bookingList;
            case "PAST":
                bookingList = bookingRepository.findByBookerIdAndEndBeforeOrderByEndDesc(userId,LocalDateTime.now());
                log.info("Вернулись брони вещей в количестве = {}, c параметром выборки {}", bookingList.size(), state);
                return bookingList;
            case "UNSUPPORTED_STATUS":
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
            default:
                bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
                log.info("Вернулись брони вещей в количестве = {}, по умолчанию ", bookingList.size());
                return bookingList;
        }

    }

    @Override
    public Collection<Booking> getBookingsForOwner(long userId) { //для владельца бронирований
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Не найден пользователь # при запросе бронирований этим пользователем", userId);
        }

        List<Booking> bookingList = bookingRepository.findByItemOwnerIdOrderByIdDesc(userId);

        log.info("Вернулись брони вещей в количестве {}, запрощенных владельцем {} броней ",bookingList.size(),userId);

        return bookingList;
    }

    @Override
    public Collection<Booking> getBookingsOwnerState(long userId, String state) { //для владельца бронирований
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Не найден пользователь # при запросе бронирований этим пользователем", userId);
        }

        List<Booking> bookingOwnerList;
        switch (state) {
            case "ALL":
                bookingOwnerList = bookingRepository.findByItemOwnerIdOrderByIdDesc(userId);
                log.info("Вернулись брони вещей в количестве {}, запрощенных владельцем {} броней, с ",
                        bookingOwnerList.size(),userId);
                return bookingOwnerList;
            case "FUTURE":
                bookingOwnerList = bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
                log.info("Вернулись брони вещей в количестве {}, запрощенных владельцем {} броней ",
                        bookingOwnerList.size(),userId);
                return bookingOwnerList;
            case "WAITING":
                bookingOwnerList = bookingRepository.findByItemOwnerIdAndStatusOrderByIdDesc(userId,Status.WAITING);
                log.info("Вернулись брони вещей в количестве {}, " +
                        "запрощенных владельцем {} броней со статусом WAITING",bookingOwnerList.size(),userId);
                return bookingOwnerList;
            case "REJECTED":
                bookingOwnerList = bookingRepository.findByItemOwnerIdAndStatusOrderByIdDesc(userId,Status.REJECTED);
                log.info("Вернулись брони вещей в количестве {}, " +
                        "запрощенных владельцем {} броней со статусом REJECTED",bookingOwnerList.size(),userId);
                return bookingOwnerList;
            case "CURRENT":
                bookingOwnerList = bookingRepository.findByBookingCurrentForOwner(userId,LocalDateTime.now());
                log.info("Вернулись брони вещей в количестве {}, " +
                        "запрощенных владельцем {} броней со статусом CURRENT",bookingOwnerList.size(),userId);
                return bookingOwnerList;
            case "PAST":
                bookingOwnerList = bookingRepository
                        .findByItemOwnerIdAndEndBeforeOrderByEndDesc(userId,LocalDateTime.now());
                log.info("Вернулись брони вещей в количестве {}, " +
                        "запрощенных владельцем {} броней со статусом PAST",bookingOwnerList.size(),userId);
                return bookingOwnerList;
            case "UNSUPPORTED_STATUS":
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
            default:
                bookingOwnerList = bookingRepository.findByItemOwnerIdOrderByIdDesc(userId);
                log.info("Вернулись брони вещей в количестве {}, запрощенных владельцем {} броней, по умолчанию",
                        bookingOwnerList.size(),userId);
                return bookingOwnerList;

        }
    }

    private void validDate(CreateBooking createBooking) {
        LocalDateTime start = createBooking.getStart();
        LocalDateTime end = createBooking.getEnd();

        int equals = start.compareTo(end);

        if (equals == 0) {
            throw new BadRequestException(
                    "Время начала # бронирования не может быть равно времени окончания #",start,end
            );
        }

        if (equals > 0) {
            throw new BadRequestException(
                    "Время начала # бронирования не может быть позже времени окончания #",start,end
            );
        }

        if (start.compareTo(LocalDateTime.now()) < 0) {
            throw new BadRequestException("Время начала # бронирования не может быть в прошлом!",start);
        }
    }
}

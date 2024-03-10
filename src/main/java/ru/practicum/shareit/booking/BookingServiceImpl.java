package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBooking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final BookingMapper mapper;

    public BookingDto addBooking(CreateBooking createBooking, Long userId) {

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

        return mapper.toBookingDto(newBooking);
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

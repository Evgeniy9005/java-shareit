package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBooking;

public interface BookingService {
    BookingDto addBooking(CreateBooking createBooking, Long userId);
}

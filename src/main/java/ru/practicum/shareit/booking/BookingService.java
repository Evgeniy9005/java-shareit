package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.CreateBooking;

import java.util.Collection;

public interface BookingService {
    Booking addBooking(CreateBooking createBooking, Long userId);

    Booking setStatus(long bookingId, long userId, Boolean approved);

    Booking getBookingByIdForUserId(long bookingId, long userId);

    Collection<Booking> getBookingsForUser(long userId, String state);

    Collection<Booking> getBookingsForOwner(long userId);

    Collection<Booking> getBookingsOwnerState(long userId, String state);
}

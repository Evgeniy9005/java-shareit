package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoNoTime;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBooking(BookingDto bookingDto);

    BookingDto toBookingDto(Booking booking);

    BookingDtoNoTime toBookingDtoNoTime(Booking booking);
}

package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBooking(BookingDto bookingDto);

    BookingDto toBookingDto(Booking booking);
}

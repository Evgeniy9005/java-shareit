package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.booking.IndicatorBooking;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class ItemDto {

    private final Long id;

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

    @NotNull
    private final Boolean available;

    private final User owner;

    private final Long request;
    @Builder.ObtainVia(method = "setLastNextBookings")
    private IndicatorBooking lastBooking;
    @Builder.ObtainVia(method = "setLastNextBookings")
    private IndicatorBooking nextBooking;
    @Builder.Default
    private final Collection<CommentDto> comments = new ArrayList<>();

    public void setLastNextBookings(List<Booking> bookingsList){
        if (bookingsList != null && bookingsList.size() >= 2) {
            Booking bookingLast = bookingsList.get(0);
            Booking bookingNext = bookingsList.get(1);
            this.lastBooking = new IndicatorBooking(bookingLast.getId(),bookingLast.getBooker().getId());
            this.nextBooking = new IndicatorBooking(bookingNext.getId(),bookingNext.getBooker().getId());
        }
    }
}

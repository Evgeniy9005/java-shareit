package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class itemDtoLastNextBooking {

    private final Long id;

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

    @NotNull
    private final Boolean available;

    private final User owner;

    private final Long request;

    private Booking lastBooking;

    private Booking nextBooking;
}

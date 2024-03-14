package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.booking.IndicatorBooking;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;


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

    private final IndicatorBooking lastBooking;

    private final IndicatorBooking nextBooking;

    @Builder.Default
    private final Collection<CommentDto> comments = new ArrayList<>();

}

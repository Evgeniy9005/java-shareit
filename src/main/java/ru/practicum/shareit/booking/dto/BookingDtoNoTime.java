package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@ToString
@Getter
@RequiredArgsConstructor
public class BookingDtoNoTime {

    private final long id;

    private final String start;

    private final String end;

    private final Item item;

    private final User booker;

    private final String status;
}

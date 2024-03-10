package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@RequiredArgsConstructor
public class CreateBooking {

    @NotNull
    private final Long itemId;

    @NotNull
    private final LocalDateTime start;

    @NotNull
    private final LocalDateTime end;

}

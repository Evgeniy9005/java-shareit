package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemRequestDto {

    private final long id;

    private final String description;

    private final LocalDateTime created;

    private final long requester;

    @Builder.Default
    private final List<Item> items = new ArrayList<>();

}

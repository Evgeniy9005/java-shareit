package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */

@Builder(toBuilder = true)
public class ItemDto {
    private final long id;

    private final String name;

    private final String description;

    private final boolean available;

    private final User owner;

    private final ItemRequest request;
}

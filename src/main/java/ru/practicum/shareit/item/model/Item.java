package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class Item {
    private final int id;
    private final String name;
    private final String description;
    private final boolean available;
    private final User owner;
    private final ItemRequest request;

}

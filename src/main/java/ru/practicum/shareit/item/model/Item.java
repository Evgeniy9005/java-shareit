package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder(toBuilder = true)
public class Item {

    private final long id;

    private final String name;

    private final String description;

    private final boolean available;

    private final User owner;

    private final ItemRequest request;

}

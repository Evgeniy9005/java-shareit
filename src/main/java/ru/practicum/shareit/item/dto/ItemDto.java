package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

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

    public Item toUser() {

        return Item.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .available(this.available)
                .owner(this.owner)
                .request(this.request)
                .build();
    }

    public static ItemDto toUserDto(Item item) {

        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
    }
}

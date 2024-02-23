package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto upItem(ItemDto itemDto, long itemId);
}

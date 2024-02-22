package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    private long generationId = 0;

    public Item addItem(Item item) {
        long id = ++generationId;

        items.put(id,item.toBuilder().id(id).build());
        return items.get(id);
    }

    public Item getItem(long itemId) {
        if(!items.containsKey(itemId)) {
            throw new NotFoundException("Не найдена вещь по id = "+itemId);
        }

        return items.get(itemId);
    }
}

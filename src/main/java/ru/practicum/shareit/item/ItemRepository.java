package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepository {

    private final Map<Long, Map<Long, Item>> userAndItems = new HashMap<>();

    private long generationId = 0;

    public Item addItem(Item item) {

        long userId = item.getOwner();

        long itemId = ++generationId;

        Item newItem = item.toBuilder().id(itemId).build();

        if (userAndItems.containsKey(userId)) {
        userAndItems.get(userId).put(itemId,newItem);
        } else {
            Map<Long, Item> itemMap = new HashMap<>();
            itemMap.put(itemId, newItem);
            userAndItems.put(userId,itemMap);
        }

        return userAndItems.get(userId).get(itemId);
    }

    public Item upItem(Item item) {
        long itemId = item.getId();
        long userId = item.getOwner();

        if (userAndItems.get(userId).containsKey(itemId)) {
            userAndItems.get(userId).remove(itemId);
            userAndItems.get(userId).put(itemId, item);

        } else {
            throw new NotFoundException("При обновлении не найдена вещь под id = " + itemId);
        }

        return userAndItems.get(userId).get(itemId);
    }

    public Item getItem(long itemId, long userId) {

        this.isItem(itemId, userId);

        return userAndItems.get(userId).get(itemId);
    }

    public Item getItemByRequestUsers(long itemId) {

        Map<Long,Item> allItems = getAllItems();

        if (!allItems.containsKey(itemId)) {
            throw new NotFoundException(String.format("Не найдена вещь под id = %s",itemId));
        }

        return  allItems.get(itemId);
    }

    public Collection<Item> getItemsByUserId(long userId) {


        if (!userAndItems.containsKey(userId)) {
            throw new NotFoundException(String.format("У пользователя под id = %s нет добавленных вещей ", userId));
        }

        return userAndItems.get(userId).values();
    }

    public boolean isItem(long itemId, long userId) {

        if (!userAndItems.containsKey(userId)) {
            throw new NotFoundException(String.format("У пользователя под id = %s нет добавленных вещей ",userId));
        }

        if (!userAndItems.get(userId).containsKey(itemId)) {
            throw new NotFoundException(
                    String.format("Не найдена вещь под id = %s у пользователя под id = %s",itemId, userId)
                    );
        }

        return true;
    }

    public Collection<Item> search(String text) {

        return getAllItems().values().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getDescription()
                                .toLowerCase()
                                .contains(text.toLowerCase()))
                .collect(Collectors.toSet());
    }

    private Map<Long,Item> getAllItems() {

        return userAndItems.values().stream()
                .flatMap(itemMap -> itemMap.entrySet().stream())
                .collect(Collectors.toConcurrentMap(Map.Entry::getKey,Map.Entry::getValue));
    }
}

package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepository {

    private final UserRepository userRepository;
    private final Map<Long, Item> items = new HashMap<>();

    private final Map<Long, Set<Item>> userAndItems = new HashMap<>();


    private long generationId = 0;

    @Autowired
    public ItemRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Item addItem(Item item) {

        long userId = item.getOwner();

        userRepository.isUser(userId);

        long id = ++generationId;

        Item newItem = item.toBuilder().id(id).build();

        if(userAndItems.containsKey(userId)) {
        userAndItems.get(userId).add(newItem);
        } else {
            userAndItems.put(userId,new HashSet<>(Set.of(newItem)));
        }

        items.put(id,newItem);

        return items.get(id);
    }

    public Item upItem(Item item) {
        long id = item.getId();
        long userId = item.getOwner();

        userRepository.isUser(userId);

        if (items.containsKey(id)) {
            items.remove(id);
            items.put(id,item);

            if(userAndItems.containsKey(userId)) {
                userAndItems.get(userId).add(item);
            } else {
                userAndItems.put(userId,new HashSet<>(Set.of(item)));
            }

        } else {
            throw new NotFoundException("При обновлении не найдена вещь под id = " + id);
        }

        return items.get(id);
    }

    public Item getItem(long itemId) {
        if(!items.containsKey(itemId)) {
            throw new NotFoundException("Не найдена вещь по id = "+itemId);
        }

        return items.get(itemId);
    }

    public Collection<Item> getItems(){
        return items.values();
    }

    public Set<Item> getItemsByUserId(long userId) {
        return userAndItems.get(userId);
    }

    public boolean isItem(long itemId) {

        if(items.containsKey(itemId)) {
            throw new NotFoundException("Не найдена вещь под id = " + itemId);
        }

        return true;
    }

}

package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.data.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRepository itemRepository;

    List<User> savedUser;

    List<ItemRequest> savedItemRequest;

    List<Item> savedItems;

    @BeforeEach
    void start() {
        savedUser = Data.<User>generationData(2,User.class)
                .stream()
                .map(user -> userRepository.save(user))
                .collect(Collectors.toList());
        Data.printList(savedUser,"*");

        savedItemRequest = Data.<ItemRequest>generationData(2,ItemRequest.class)
                .stream()
                .map(itemRequest -> itemRequestRepository.save(itemRequest))
                .collect(Collectors.toList());
        Data.printList(savedItemRequest,">");

        savedItems = Data.<Item>generationData(5,Item.class,savedUser.get(0),1L)
                .stream()
                .map(item -> itemRepository.save(item))
                .collect(Collectors.toList());
        Data.printList(savedItems,"^");

    }


    @Test
    void findByOwnerId() {

    }

    @Test
    void searchByIgnoreCaseDescriptionContainingAndAvailableTrue() {

    }

    @Test
    void findByRequest() {
      List<Item> itemList = itemRepository.findByRequest(1l);
      assertEquals(5,itemList.size());

    }

}
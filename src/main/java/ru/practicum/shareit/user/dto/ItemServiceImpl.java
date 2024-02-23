package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {

            userRepository.isUser(userId);

            Item newItem = itemRepository.addItem(itemMapper.toItem(itemDto).toBuilder().owner(userId).build());

            log.info("Добавлена вещь {}",newItem);

        return itemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto upItem(ItemDto itemDto, long itemId) {

        itemRepository.isItem(itemId);

        Item updateItem = itemRepository.upItem(itemMapper.toItem(itemDto).toBuilder().build());

        return itemMapper.toItemDto(updateItem);
    }


}

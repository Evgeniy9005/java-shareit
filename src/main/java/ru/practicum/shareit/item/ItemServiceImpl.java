package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

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
    public ItemDto upItem(ItemDto itemDto, long itemId, long userId) {

        if (userId <= 0) {
            throw new BadRequestException("Не коректный id пользователя = " + userId);
        }

        if (itemDto.getOwner() != userId) {
            throw new NotFoundException("Не владелец этой вещи пользователь под id " + userId);
        }

        userRepository.isUser(userId);

        itemRepository.isItem(itemId, userId);

        Item updateItem = itemRepository.upItem(itemMapper.toItem(itemDto).toBuilder().build());

        return itemMapper.toItemDto(updateItem);
    }

    @Override
    public ItemDto getItem(long itemId, long userId) {

        return itemMapper.toItemDto(itemRepository.getItem(itemId, userId));
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(long userId) {

        return itemRepository.getItemsByUserId(userId).stream()
                .map(item -> itemMapper.toItemDto(item))
                .collect(Collectors.toSet());
    }

    @Override
    public ItemDto getItemByRequestUsers(long itemId, long userIdMakesRequest) {
        return itemMapper.toItemDto(itemRepository.getItemByRequestUsers(itemId,userIdMakesRequest));
    }

    @Override
    public Collection<ItemDto> search(String text, long userId) {

        if (text.isBlank()) {
        return new ArrayList<>(0);
        }

        return itemRepository.search(text,userId).stream()
                .map(item -> itemMapper.toItemDto(item))
                .collect(Collectors.toSet());
    }

}


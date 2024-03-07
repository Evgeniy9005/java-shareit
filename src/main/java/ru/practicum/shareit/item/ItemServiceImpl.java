package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final ItemDao itemDao;

    private final UserDao userDao;

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {

        Item newItem = itemDao.save(
                itemMapper.toItem(itemDto.toBuilder().owner(userDao.getById(userId)).build())
        );

        log.info("Добавлена вещь {}",newItem);

        return itemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto upItem(ItemDto itemDto, long itemId, long userId) {

        if (userId <= 0) {
            throw new BadRequestException("Не коректный id пользователя = " + userId);
        }

       /* if (itemDto.getOwner() != userId) {
            throw new NotFoundException("Не владелец этой вещи пользователь под id " + userId);
        }*/


        itemRepository.isItem(itemId, userId);

        Item updateItem = itemRepository.upItem(itemMapper.toItem(itemDto));

        log.info("Обновлена вещь {}",updateItem);

        return itemMapper.toItemDto(updateItem);
    }

    @Override
    public ItemDto getItem(long itemId, long userId) {

        log.info("Вернуть вещь {}, пользователь {}", itemId, userId);

        return itemMapper.toItemDto(itemRepository.getItem(itemId, userId));
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(long userId) {

        log.info("Вернуть все вещи пользователя {}", userId);

        return itemRepository.getItemsByUserId(userId).stream()
                .map(item -> itemMapper.toItemDto(item))
                .collect(Collectors.toSet());
    }

    @Override
    public ItemDto getItemByRequestUsers(long itemId, long userIdMakesRequest) {

        log.info("Вернуть вещь {} по запросу пользователя {}", itemId, userIdMakesRequest);

        return itemMapper.toItemDto(itemRepository.getItemByRequestUsers(itemId));
    }

    @Override
    public Collection<ItemDto> search(String text, long userId) {

        log.info("Поиск вещей по тексту {}, по запросу пользователя {}", text, userId);

        if (text.isBlank()) {
        return new ArrayList<>(0);
        }

        return itemRepository.search(text).stream()
                .map(item -> itemMapper.toItemDto(item))
                .collect(Collectors.toSet());
    }

}


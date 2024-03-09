package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;

    private final ItemRepository itemRepository;

    private final UserRepository userDao;


    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {

        Item newItem = itemRepository.save(
                itemMapper.toItem(itemDto.toBuilder().owner(
                        userDao.findById(userId).orElseThrow(
                                ()-> new NotFoundException(
                                        "при добовлении вещи не найден пользователь под id = " + userId))
                ).build())
        );

        log.info("Добавлена вещь {}",newItem);

        return itemMapper.toItemDto(newItem);
    }


    @Override
    public ItemDto upItem(ItemDto itemDto, long itemId, long userId) {

       // itemMapper.toItem(Patch.patchItemDto())

        if (userId <= 0) {
            throw new BadRequestException("Не коректный id пользователя = " + userId);
        }

        if (itemDto.getOwner().getId() != userId) {
            throw new NotFoundException("Не владелец этой вещи пользователь под id " + userId);
        }

        Item updateItem = itemRepository.save(itemMapper.toItem(itemDto));

        log.info("Обновлена вещь {}",updateItem);

        return itemMapper.toItemDto(updateItem);
    }

    @Override
    public ItemDto getItem(long itemId, long userId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new NotFoundException("Не найдена вещь под id = " + itemId));

        log.info("Вернулась вещь {}, пользователя {}", item, userId);

        return itemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(long userId) {

        log.info("Вернуть все вещи пользователя {}", userId);

        return itemRepository.findByOwnerId(userId).stream()
                .map(item -> itemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemByRequestUsers(long itemId, long userIdMakesRequest) {

        if (!userDao.existsById(userIdMakesRequest)) {
           throw new NotFoundException(
                   "Не найден пользователь # при запросе вещи # пользователем #",
                   userIdMakesRequest,
                   itemId,
                   userIdMakesRequest
           );
        }

        Item item = itemRepository.findById(itemId).orElseThrow(
                ()-> new NotFoundException("Не найдена вешь под id = " + itemId)
        );

        log.info("Вернулась вещь {} по запросу пользователя {}", item, userIdMakesRequest);

        return itemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> search(String text, long userId) {

        log.info("Поиск вещей по тексту {}, по запросу пользователя {}", text, userId);

        if (text.isBlank()) {
        return new ArrayList<>(0);
        }

        return itemRepository.searchByIgnoreCaseDescriptionContainingAndAvailableTrue(text).stream()
                .map(item -> itemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

}


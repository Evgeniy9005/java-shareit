package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto upItem(ItemDto itemDto, long itemId, long userId);

    ItemDto getItem(long itemId, long userId);

    Collection<ItemDto> getItemsByUserId(long userId);

    ItemDto getItemByRequestUsers(long itemId, long userIdMakesRequest);

    Collection<ItemDto> search(String text, long userId);

    CommentDto addComment(CreateCommentDto createCommentDto, long itemId, long authorId);
}

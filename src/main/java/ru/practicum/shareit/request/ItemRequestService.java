package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(CreateItemRequest createRequest , long userId);

    Collection<ItemRequestDto> getItemsRequester(long userId);

    Collection<ItemRequestDto> getItemsRequesterPagination(long userId, int from, int size);

    ItemRequestDto getItemRequestByIdForOtherUser(long userId, long requestId);
}

package ru.practicum.shareit.util;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.Valid;

public class Patch {

    @Valid
    public static ItemDto patchItemDto(ItemDto updated, ItemDto patch) {
       Long id = patch.getId();
       String name = patch.getName();
       String description = patch.getDescription();
       Boolean available = patch.getAvailable();
       Long owner = patch.getOwner();
       ItemRequest request = patch.getRequest();
       return updated.builder()
               .id(id == null ? updated.getId() : id)
               .name(name == null ? updated.getName() : name)
               .description(description == null ? updated.getDescription() : description)
               .available(available == null ? updated.getAvailable() : available)
               .owner(owner == null ? updated.getOwner() : owner)
               .request(request == null ? updated.getRequest() : request)
               .build();
    }
}

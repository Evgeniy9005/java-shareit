package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "request", source = "itemDto.requestId")
    Item toItem(ItemDto itemDto);

    @Mapping(target = "requestId", source = "item.request")
    ItemDto toItemDto(Item item);
}

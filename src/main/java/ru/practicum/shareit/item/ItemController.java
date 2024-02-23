package ru.practicum.shareit.item;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    public ItemDto addItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {

        return itemService.addItem(itemDto,userId);
    }

    @PatchMapping
    public ItemDto upItem(@RequestBody @Valid JsonPatch jsonPatch, @PathVariable Long itemId) {


        return ItemDto.builder().build();
    }
}

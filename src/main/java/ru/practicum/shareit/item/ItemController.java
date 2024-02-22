package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

/**
 * TODO Sprint add-controllers.
 */


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    @PostMapping
    public Item addItem(@RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Long userId) {

        return itemService.addItem(item,userId);
    }
}

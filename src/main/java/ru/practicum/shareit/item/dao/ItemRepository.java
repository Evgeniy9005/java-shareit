package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;


public interface ItemRepository extends JpaRepository<Item,Long> {

    Collection<Item> findByOwnerId(Long userId);

    List<Item> searchByIgnoreCaseDescriptionContainingAndAvailableTrue(String text);

    List<Item> findByRequest(long requestId);

    /*@Query("select new ru.practicum.shareit.request.dto.ItemRequestDto(r.id,r.description,r.created,r.requester) from ItemRequest as r")
    List<ItemDto> findByRequestId(long requestId);*/
}

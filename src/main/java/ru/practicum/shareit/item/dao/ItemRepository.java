package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;


public interface ItemRepository extends JpaRepository<Item,Long> {

    Collection<Item> findByOwnerId(Long userId);

    List<Item> searchByIgnoreCaseDescriptionContainingAndAvailableTrue(String text);

    List<Item> findByRequest(long requestId);

}

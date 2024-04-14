package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import java.util.List;


public interface ItemRepository extends JpaRepository<Item,Long> {

    List<Item> findByRequest(long requestId);

    List<Item> findByOwnerId(Long userId, Pageable pageable);

    List<Item> searchByIgnoreCaseDescriptionContainingAndAvailableTrue(String text, Pageable pageable);

}

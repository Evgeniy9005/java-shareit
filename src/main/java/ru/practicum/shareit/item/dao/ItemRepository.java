package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import java.util.List;


public interface ItemRepository extends JpaRepository<Item,Long> {

   /* Collection<Item> findByOwnerId(Long userId);

    List<Item> searchByIgnoreCaseDescriptionContainingAndAvailableTrue(String text);*/

    List<Item> findByRequest(long requestId);

    /*@Query("select new ru.practicum.shareit.request.dto.ItemRequestDto(r.id,r.description,r.created,r.requester) from ItemRequest as r")
    List<ItemDto> findByRequestId(long requestId);*/

    List<Item> findByOwnerId(Long userId, Pageable pageable);

    List<Item> searchByIgnoreCaseDescriptionContainingAndAvailableTrue(String text, Pageable pageable);

    //List<Item> findByRequest(long requestId, Pageable pageable);
}

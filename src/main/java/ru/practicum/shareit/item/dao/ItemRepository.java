package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import java.util.List;


public interface ItemRepository extends JpaRepository<Item,Long> {

    List<Item> findByRequest(long requestId);

    List<Item> findByOwnerId(Long userId, Pageable pageable);

    List<Item> searchByIgnoreCaseDescriptionContainingAndAvailableTrue(String text, Pageable pageable);


    /*@Query("select new ru.practicum.shareit.item.dto.itemdtolastnextbooking(ij.id," +
            "ij.name, " +
            "ij.description, " +
            "ij.available, " +
            "ij.owner.id, " +
            "ij.request, " +
            "case " +
            "when i.c > 2 then select b.id from Booking as b where b.item.id = i.item.id limit 1,1 " +
            "when i.c = 2 or i.c = 1 then select b.id from Booking as b where b.item.id = i.item.id limit 1 " +
            "end as lastbooking, " +
            "case " +
            "when i.c > 2 then select b.id from Booking as b where b.item.id = i.item.id order by b.id desc limit 1,1 " +
            "when i.c = 2 then select b.id from Booking as b where b.item.id = i.item.id order by b.id desc limit 1 " +
            "end as nextbooking) " +
            "from Item as ij " +
            "left outer join " +
            "(select b.item.id, count(b.item.id) as c " +
            "from Booking as b " +
            "group by  b.item.id) as i on i.item.id = ij.id where ij.owner.id = ?1 ")
    List<ItemDtoLastNextBooking> findByOwnerItemsLastNextBooking(long ownerId);*/

}

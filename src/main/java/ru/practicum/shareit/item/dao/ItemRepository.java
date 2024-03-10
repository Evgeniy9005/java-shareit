package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {
    //select item where owner == 1
    //@Query(value = "SELECT * FROM ITEMS WHERE OWNER_ID = ?1", nativeQuery = true)
   /* @Modifying
    @Query("select i from Item i where i.owner.id = ?1")*/
    Collection<Item> findByOwnerId(Long userId);

    /*@Modifying
    @Query("select i from Item i where lower(i.description) like lower(%?1%)")*/
    List<Item> searchByIgnoreCaseDescriptionContainingAndAvailableTrue(String text);

    //
    Optional<Item> findByIdAndAvailableTrue(long userId);

}

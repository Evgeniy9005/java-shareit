package ru.practicum.shareit.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.shareit.request.ItemRequest;
import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest,Long> {

    List<ItemRequest> findByRequester(long userId);
   /* @Query("select ru.practicum.shareit.request.dto.ItemRequestDto.builder()\n" +
            "              .id(r.id)\n" +
            "              .description(r.description)\n" +
            "              .created(r.created)\n" +
            "              .requester(r.requester)\n" +
            "              .build() from ItemRequest as r where r.id = ?1")*/
   // List<ItemRequestDto> findByRequestAndItems(long requestId);
  //@Query("select new ItemRequestDto(1,description",LocalDateTime.now(),1)")

   /* @Query("select new ru.practicum.shareit.request.dto.ItemRequestDto(r.id,r.description,r.created,r.requester) from ItemRequest as r")
    List<ItemRequestDto> findByRequestAndItems();*/
}

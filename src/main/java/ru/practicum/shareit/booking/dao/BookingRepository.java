package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByItemOwnerIdOrderByIdDesc(long ownerId);

    List<Booking> findByItemOwnerIdOrderByStartDesc(long ownerId);

    List<Booking> findByItemOwnerIdAndStatusOrderByIdDesc(long ownerId, Status state);

    List<Booking> findByBookerIdAndStatusOrderByIdDesc(long bookerId, Status state);

    /*@Modifying
    @Query("select b from bookings b where b.item.id ?1 order by b.start limit ?2")
    List<Booking> findByItemIdOrderByStartAsc(long itemId, int limit);*/
    List<Booking> findByItemIdAndItemOwnerIdOrderByStartAsc(long itemId,long userId);


}

package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByItemOwnerIdOrderByIdDesc(long ownerId);

    List<Booking> findByItemOwnerIdOrderByStartDesc(long ownerId);

    List<Booking> findByItemOwnerIdAndStatusOrderByIdDesc(long ownerId, Status state);

    List<Booking> findByBookerIdAndStatusOrderByIdDesc(long bookerId, Status state);

    /*@Modifying
    @Query("select b from bookings b where b.item.id ?1 order by b.start limit ?2")
    List<Booking> findByItemIdOrderByStartAsc(long itemId, int limit);*/
    //List<Booking> findByItemIdAndItemOwnerIdOrderByStartAsc(long itemId,long userId);
    List<Booking> findByItemIdAndItemOwnerIdAndStatusOrderByStartAsc(long itemId,long ownerId, Status approved);

    //наличие вещи в аренде
    boolean existsByItemIdAndBookerIdAndStatusAndEndBefore(
            long itemId, long bookerId, Status approved, LocalDateTime end);

    //текущие бронирования созданные пользователем
    @Query("select b from Booking b where b.booker.id = ?1 and b.start <= ?2 and b.end >= ?2 order by b.id asc")
    List<Booking> findByBookingCurrentForBooker(long bookerId, LocalDateTime current);

    //текущие бронирования хозяина вещей
    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start <= ?2 and b.end >= ?2 order by b.id asc")
    List<Booking> findByBookingCurrentForOwner(long ownerId, LocalDateTime current);

    List<Booking> findByBookerIdAndEndBeforeOrderByEndDesc(long bookerId, LocalDateTime past);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByEndDesc(long ownerId, LocalDateTime past);
}

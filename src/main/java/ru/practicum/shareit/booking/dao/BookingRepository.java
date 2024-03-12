package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByItemOwnerIdOrderByIdDesc(long ownerId);

    List<Booking> findByItemOwnerIdOrderByStartDesc(long bookerId);

}

package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@NoArgsConstructor
public class Booking {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "start_date")
    private LocalDate start = LocalDate.now();
    @Column(name = "end_date")
    private LocalDate end;
    @Column
    @JoinColumn(name = "item_id")
    private Item item;
    @Column
    @JoinColumn(name = "booker_id")
    private User booker;
    @Column
    @JoinColumn(name = "status_id")
    private Status status;

}

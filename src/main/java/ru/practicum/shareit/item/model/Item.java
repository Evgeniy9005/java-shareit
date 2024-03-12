package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.user.User;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity
@NoArgsConstructor
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "request_id")
    private long request;

   /* @OneToMany(mappedBy = "item")//сыслка на поле item в сущьности booking
    private Set<Booking> bookings = new HashSet<>();

    public void addBookings(Booking booking) { //метод обновления обеих сторон ассоциаций
        this.bookings.add(booking);
        booking.setItem(this);
    }*/
}

package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="item_requests", schema = "public")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

 /*   @ManyToOne
    @Column(name = "requester_id", nullable = false)
    private User requester;
*/
    @Column(name = "requester_id", nullable = false)
    private long requester;

 /*   @ElementCollection
    @CollectionTable(name="items", joinColumns=@JoinColumn(name="request_id"))
    @Column(name="name")*/

/*    @OneToMany
    @JoinColumn(name = "request_id")
    private List<Item> items = new ArrayList<>();*/
}

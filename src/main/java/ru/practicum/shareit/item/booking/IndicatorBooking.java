package ru.practicum.shareit.item.booking;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.Booking;

@Getter
@ToString
public class IndicatorBooking {

    private long id;
    private long bookerId;

    public IndicatorBooking getIndicatorBooking(Booking booking){
        this.id = booking.getId();
        this.bookerId = booking.getBooker().getId();
        return this;
    }
}

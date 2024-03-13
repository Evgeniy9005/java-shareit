package ru.practicum.shareit.item.booking;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.Booking;

@Getter
@ToString
@RequiredArgsConstructor
public class IndicatorBooking {

    private final long id;
    private final long bookerId;


   /* public IndicatorBooking getIndicatorBooking(Booking booking){
        this.id = booking.getId();
        this.bookerId = booking.getBooker().getId();
        return this;
    }*/
}

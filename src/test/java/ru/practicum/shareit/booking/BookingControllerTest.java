package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.user.UserService;

@SpringBootTest
class BookingControllerTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingMapper mapper;



    @Test
    void addBooking() {
    }

    @Test
    void setStatus() {
    }

    @Test
    void getBookingByIdForUserId() {
    }

    @Test
    void getBookingsForUser() {
    }

    @Test
    void getBookingsForOwner() {
    }
}
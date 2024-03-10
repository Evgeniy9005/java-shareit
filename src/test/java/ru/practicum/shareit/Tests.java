package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.CreateBooking;
import ru.practicum.shareit.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class Tests {

    @Test
    void contextLoads() {
       // throw new NotFoundException("# #","1",1,1);
        System.out.println("qwwqTTThhhh".toLowerCase());
    }

    @Test
    void validDate() {
        LocalDateTime start = LocalDateTime.of(2023,1,1,1,0);
        LocalDateTime end = LocalDateTime.of(2023,1,1,1,1);

        if (start.compareTo(end) > 0) {
            System.out.println(start+" start.compareTo(end) " + end + " = "+ start.compareTo(end));
        }

        if (start.compareTo(end) == 0) {
            System.out.println(start+" start.compareTo(end) " + end + " = "+ start.compareTo(end));
        }

        if (start.compareTo(end) < 0) {
            System.out.println(start+" start.compareTo(end) " + end + " = "+ start.compareTo(end));
        }
    }
}

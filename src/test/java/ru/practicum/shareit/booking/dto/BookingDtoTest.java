package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.data.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> jacksonTester;


    private BookingMapper bookingMapper;


    List<Booking> bookingList;

    List<User> userList;

    List<Item> itemList;

    @BeforeEach
    void start() {
        bookingMapper = new BookingMapperImpl();
        userList = Data.<User>generationData(1,User.class);
        itemList = Data.<Item>generationData(1,Item.class,userList.get(0),1L);
        bookingList = Data.<Booking>generationData(1,Booking.class,userList.get(0),itemList.get(0));
    }

    @Test
    void testBookingDto() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024,1,1,1,1,1);
        LocalDateTime end = LocalDateTime.of(2024,1,1,1,1,1);

        BookingDto bookingDto = bookingMapper.toBookingDto(bookingList.get(0)).toBuilder()
                .start(start)
                .end(end)
                .build();

        Item item = bookingDto.getItem();

        User booker = bookingDto.getBooker();

        JsonContent<BookingDto> result = jacksonTester.write(bookingDto);
        System.out.println(result);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.toString());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo("описание вещи 1");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("User1");
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo("user1@mail");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");

    }

}
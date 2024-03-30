package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.mapper.BookingMapper;
//import ru.practicum.shareit.data.Data;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;
import static ru.practicum.shareit.data.Data.*;
@SpringBootTest
class ShareItTests {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ItemService itemService;

	private ItemMapper itemMapper;

	@Autowired
	private ItemRequestService itemRequestService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private BookingMapper bookingMapper;



	List<UserDto> userDtoList;

	List<ItemDto> itemDtoList;
	List<User> users;
	List<Item> items;



	@BeforeEach
	void start() {
		users = generationData(2,User.class);
		userDtoList = users.stream().map(user -> userMapper.toUserDto(user)).collect(Collectors.toList());
		printList(userDtoList,"===");

		items = generationData(2,Item.class,users.get(0),1L);
		printList(items,"ccc");
		/*itemDtoList = items.stream().map(item -> itemMapper.toItemDto(item)).collect(Collectors.toList());
		printList(itemDtoList,"xxx");*/

	}


	@Test
	void addUser() {
		System.out.println(userDtoList);
		UserDto userDto = userService.addUser(userDtoList.get(0));
		System.out.println(userDto);
	}

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

	@Test
	void contextLoads() {

	}

}

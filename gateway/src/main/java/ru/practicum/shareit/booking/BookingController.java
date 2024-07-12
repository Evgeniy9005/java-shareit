package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.CreateBooking;
import ru.practicum.shareit.excteption.UnsupportedStatusException;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestBody @Valid CreateBooking createBooking,
											 @Positive @RequestHeader("X-Sharer-User-Id") Long userId
	) {
		log.info("Запрос бронирования createBooking {} от пользователя {}", createBooking, userId);
		return bookingClient.addBooking(createBooking,userId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> setStatus(@PathVariable Long bookingId,
											@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
											@RequestParam Boolean approved
	) {
		log.info("Изменение статуса бронирования {} от пользователя {}, одобрение = {}",bookingId,userId,approved);
		return bookingClient.setStatus(bookingId,userId,approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingByIdForUserId(@PathVariable Long bookingId,
														  @Positive @RequestHeader("X-Sharer-User-Id") Long userId
	) {
		log.info("Вернуть бронирование {} для пользователя {}",bookingId,userId);
		return bookingClient.getBookingByIdForUserId(bookingId,userId);
	}

	@GetMapping
	public ResponseEntity<Object> getBookingsForBooker(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
			@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
			@Positive @RequestParam (defaultValue = "10") Integer size
	) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + stateParam));
		log.info("Вернуть бронирования вещей с state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookingsForBooker(userId,state,from,size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsForOwner(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
			@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
			@Positive @RequestParam(defaultValue = "10") Integer size
	) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + stateParam));
		log.info("Вернуть бронирования вещей state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookingsForOwner(userId,state,from,size);
	}
}

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBooking;
import ru.practicum.shareit.booking.mapper.BookingMapper;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private final BookingMapper mapper;

    @PostMapping
    public BookingDto addBooking(@RequestBody @Valid CreateBooking createBooking,
                                 @RequestHeader("X-Sharer-User-Id") Long userId
    ) {

        return mapper.toBookingDto(bookingService.addBooking(createBooking, userId));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setStatus(@NotNull @PathVariable Long bookingId,
                                      @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestParam Boolean approved
    ) {
        return mapper.toBookingDto(bookingService.setStatus(bookingId,userId,approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingByIdForUserId(@NotNull @PathVariable Long bookingId,
                                              @NotNull @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return mapper.toBookingDto(bookingService.getBookingByIdForUserId(bookingId,userId));
    }

    @GetMapping
    public Collection<BookingDto> getBookingsForBooker(
            @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "default") String state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {

        return bookingService.getBookingsForBooker(userId,state,from,size).stream()
                .map(booking -> mapper.toBookingDto(booking))
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingsForOwner(
            @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "default") String state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {

        return bookingService.getBookingsOwnerState(userId, state, from, size).stream()
                .map(booking -> mapper.toBookingDto(booking))
                .collect(Collectors.toList());

       /* if (state != null) {
            return bookingService.getBookingsOwnerState(userId,state,from,size).stream()
                    .map(booking -> mapper.toBookingDto(booking))
                    .collect(Collectors.toList());
        }

        return bookingService.getBookingsForOwner(userId).stream()
                .map(booking -> mapper.toBookingDto(booking))
                .collect(Collectors.toList());*/
    }
}

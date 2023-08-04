package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                @Validated(Create.class) @RequestBody BookingDto bookingDto) {
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                      @PathVariable @Positive Long bookingId,
                                                      @RequestParam Boolean approved) {
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                             @PathVariable @Positive Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookerBookings(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                    @RequestParam(defaultValue = "ALL") String state,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "20") @Positive int size) {
        BookingState bookingState = BookingState.get(state);
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookings(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                             @RequestParam(defaultValue = "ALL") String state,
                                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                             @RequestParam(defaultValue = "20") @Positive int size) {
        BookingState bookingState = BookingState.get(state);
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getOwnerBooking(userId, bookingState, from, size);
    }
}


package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.Positive;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                    @Validated(Create.class) @RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDto, userId));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> updateBookingStatus(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                          @PathVariable Long bookingId,
                                                          @RequestParam Boolean approved) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(bookingId, approved, userId));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                 @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId, userId));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getUserBookings(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                            @RequestParam(defaultValue = "ALL") String state) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId, BookingState.get(state)));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getOwnerBookings(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                             @RequestParam(defaultValue = "ALL") String state) {
        return ResponseEntity.ok(bookingService.getOwnerBookings(userId, BookingState.get(state)));
    }
}


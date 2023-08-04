package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Util;
import ru.practicum.Variables;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(Variables.USER_ID) Long userId,
                                    @RequestBody BookingDto bookingDto) {
        Booking booking = bookingService.createBooking(bookingDto, userId);
        return bookingMapper.toDto(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@RequestHeader(Variables.USER_ID) Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam Boolean approved) {
        Booking booking = bookingService.updateBookingStatus(bookingId, approved, userId);
        return bookingMapper.toDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(Variables.USER_ID) Long userId,
                                 @PathVariable Long bookingId) {
        Booking booking = bookingService.getBookingById(bookingId, userId);
        return bookingMapper.toDto(booking);
    }

    @GetMapping
    public List<BookingDto> getBookerBookings(@RequestHeader(Variables.USER_ID) Long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = Util.getPageable(from, size);
        List<Booking> bookings = bookingService.getBookerBookings(userId, BookingState.get(state), pageable);
        if (bookings.size() < 1) {
            return Collections.emptyList();
        }
        User booker = bookings.get(0).getBooker();
        return bookings.stream()
                .map(booking -> bookingMapper.toDtoWithUser(booking, booker))
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader(Variables.USER_ID) Long userId,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = Util.getPageable(from, size);
        return bookingService.getOwnerBookings(userId, BookingState.get(state), pageable)
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }
}


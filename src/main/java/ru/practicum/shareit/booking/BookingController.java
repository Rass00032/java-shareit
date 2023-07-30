package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Util;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                    @Validated(Create.class) @RequestBody BookingDto bookingDto) {
        Booking booking = bookingMapper.fromDto(bookingDto);
        booking = bookingService.createBooking(booking, userId, bookingDto.getItemId());
        return bookingMapper.toDto(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                          @PathVariable @Positive Long bookingId,
                                                          @RequestParam Boolean approved) {
        Booking booking = bookingService.updateBookingStatus(bookingId, approved, userId);
        return bookingMapper.toDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                 @PathVariable @Positive Long bookingId) {
        Booking booking = bookingService.getBookingById(bookingId, userId);
        return bookingMapper.toDto(booking);
    }

    @GetMapping
    public List<BookingDto> getBookerBookings(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                              @RequestParam(defaultValue = "ALL") String state,
                                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                              @RequestParam(defaultValue = "20") @Positive int size) {
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
    public List<BookingDto> getOwnerBookings(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                             @RequestParam(defaultValue = "ALL") String state,
                                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                             @RequestParam(defaultValue = "20") @Positive int size) {
        Pageable pageable = Util.getPageable(from, size);
        return bookingService.getOwnerBookings(userId, BookingState.get(state), pageable)
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }
}


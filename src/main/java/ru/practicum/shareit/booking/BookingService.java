package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, Long userId);

    BookingDto updateBookingStatus(Long bookingId, Boolean approved, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getUserBookings(Long userId, BookingState state);

    List<BookingDto> getOwnerBookings(Long userId, BookingState state);
}

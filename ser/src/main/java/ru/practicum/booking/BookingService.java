package ru.practicum.booking;

import org.springframework.data.domain.Pageable;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(BookingDto bookingDto, Long userId);

    Booking updateBookingStatus(Long bookingId, Boolean approved, Long userId);

    Booking getBookingById(Long bookingId, Long userId);

    List<Booking> getBookerBookings(Long userId, BookingState state, Pageable pageable);

    List<Booking> getOwnerBookings(Long userId, BookingState state, Pageable pageable);
}

package ru.practicum.booking;

import lombok.RequiredArgsConstructor;


import ru.practicum.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.Variables;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.repository.BookingRepository;
import ru.practicum.exception.BadStateException;
import ru.practicum.exception.NotAvailableException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public Booking createBooking(BookingDto bookingDto, Long userId) {
      User user = returnUserOrThrowException(userId);
        Item item = returnItemOrThrowException(bookingDto.getItemId());
        if (user.getId() == item.getOwner().getId()) {
            throw new NotFoundException("Ты владелец");
        }
        Booking newBooking = bookingMapper.fromDto(bookingDto);
        newBooking.setStatus(BookingStatus.WAITING);
        newBooking.setItem(item);
        newBooking.setBooker(user);
        newBooking = bookingRepository.save(newBooking);
        return newBooking;
    }

    @Transactional
    @Override
    public Booking updateBookingStatus(Long bookingId, Boolean approved, Long userId) {
        Booking booking = returnBookingOrThrowException(bookingId);
        Item item = booking.getItem();
        if (userId != item.getOwner().getId()) {
            throw new NotFoundException(Variables.USER_WITH_ID_NOT_HAVE_AVAILABLE, userId);
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new NotAvailableException("Рассмотрение");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        booking = bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Booking getBookingById(Long bookingId, Long userId) {
        Booking booking = returnBookingOrThrowException(bookingId);
        Item item = booking.getItem();
      User booker = booking.getBooker();
      User user = returnUserOrThrowException(userId);
        if (user.equals(item.getOwner()) ^ user.equals(booker)) {   // или хозяин, или автор бронирования
            return booking;
        } else {
            throw new NotFoundException(Variables.USER_WITH_ID_NOT_HAVE_AVAILABLE, userId);
        }
    }

    @Override
    public List<Booking> getBookerBookings(Long userId, BookingState state, Pageable pageable) {
      User booker = returnUserOrThrowException(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByBookerOrderByStartDesc(booker, pageable).toList();
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerAndStartBeforeAndEndAfter(booker, now, pageable).toList();
                break;
            case PAST:
                bookings = bookingRepository.findByBookerAndEndDateBeforeOrderByStartDesc(booker, now, pageable).toList();
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerAndStartDateAfterOrderByStartDesc(booker, now, pageable).toList();
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerAndStatus(booker, BookingStatus.WAITING, pageable).toList();
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerAndStatus(booker, BookingStatus.REJECTED, pageable).toList();
                break;
            default:
                throw new BadStateException("Неизвестное состояние");
        }
        return bookings;
    }

    @Override
    public List<Booking> getOwnerBookings(Long userId, BookingState state, Pageable pageable) {
        User owner = returnUserOrThrowException(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByOwnerOrderByStartDesc(owner, pageable).toList();
                break;
            case CURRENT:
                bookings = bookingRepository.findByOwnerAndStartBeforeAndEndAfterOrder(owner, now, pageable).toList();
                break;
            case PAST:
                bookings = bookingRepository.findByOwnerAndEndDateBeforeOrderByStartDesc(owner, now, pageable).toList();
                break;
            case FUTURE:
                bookings = bookingRepository.findByOwnerAndStartDateAfterOrderByStartDesc(owner, now, pageable).toList();
                break;
            case WAITING:
                bookings = bookingRepository.findByOwnerAndStatus(owner, BookingStatus.WAITING, pageable).toList();
                break;
            case REJECTED:
                bookings = bookingRepository.findByOwnerAndStatus(owner, BookingStatus.REJECTED, pageable).toList();
                break;
            default:
                    throw new BadStateException("Неизвестное состояние");
        }

        return bookings;
    }


    private Item returnItemOrThrowException(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Элемент с идентификатором:{0} не найден", itemId));
        if (!item.getAvailable()) {
            throw new NotAvailableException("Элемент с идентификатором: {0} недоступен", item.getId());
        }
        return item;
    }

    private Booking returnBookingOrThrowException(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование: {0} не найдено", bookingId));
    }

    private User returnUserOrThrowException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Variables.USER_WITH_ID_NOT_FOUND, userId));
    }
}

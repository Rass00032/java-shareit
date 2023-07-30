package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingDto createBooking(BookingDto bookingDto, Long userId) {
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
        return bookingMapper.toDto(newBooking);
    }

    @Transactional
    @Override
    public BookingDto updateBookingStatus(Long bookingId, Boolean approved, Long userId) {
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
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = returnBookingOrThrowException(bookingId);
        Item item = booking.getItem();
        User booker = booking.getBooker();
        User user = returnUserOrThrowException(userId);
        if (user.equals(item.getOwner()) ^ user.equals(booker)) {   // или хозяин, или автор бронирования
            return bookingMapper.toDto(booking);
        } else {
            throw new NotFoundException(Variables.USER_WITH_ID_NOT_HAVE_AVAILABLE, userId);
        }
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, BookingState state) {
        User booker = returnUserOrThrowException(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByBookerOrderByStartDesc(booker);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerAndStartBeforeAndEndAfterOrderByStartDesc(booker, now);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerAndEndDateBeforeOrderByStartDesc(booker, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerAndStartDateAfterOrderByStartDesc(booker, now);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerAndStatus(booker, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerAndStatus(booker, BookingStatus.REJECTED);
                break;
        }
        return bookings.stream()
                .map(booking -> bookingMapper.toDtoWithUser(booking, booker))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long userId, BookingState state) {
        User owner = returnUserOrThrowException(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByOwnerOrderByStartDesc(owner);
                break;
            case CURRENT:
                bookings = bookingRepository.findByOwnerAndStartBeforeAndEndAfterOrderByStartDesc(owner, now);
                break;
            case PAST:
                bookings = bookingRepository.findByOwnerAndEndDateBeforeOrderByStartDesc(owner, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findByOwnerAndStartDateAfterOrderByStartDesc(owner, now);
                break;
            case WAITING:
                bookings = bookingRepository.findByOwnerAndStatus(owner, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByOwnerAndStatus(owner, BookingStatus.REJECTED);
                break;
        }

        return bookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
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

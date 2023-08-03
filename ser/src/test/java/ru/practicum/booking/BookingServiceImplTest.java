package ru.practicum.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.mapper.BookingMapperImpl;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.repository.BookingRepository;
import ru.practicum.exception.NotAvailableException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.user.UserMapperImpl;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BookingServiceImplTest {

    private final LocalDateTime now = LocalDateTime.now();
    private final Pageable pageable = Pageable.unpaged();
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingMapperImpl bookingMapper;

    @Mock
    private UserMapperImpl userMapper;

    private User booker;
    private User owner;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;
    private List<Booking> bookings;

    @BeforeEach
    void setUp() {
        booker = User.builder()
                .id(1L)
                .name("John")
                .email("John-Smith@mail.com").build();
        owner = User.builder()
                .id(2L)
                .name("Angelina")
                .email("angelina@mail.com").build();
        item = Item.builder()
                .id(1L)
                .owner(owner)
                .name("Grill")
                .description("Hot grill")
                .available(true).build();
        booking = Booking.builder()
                .item(item)
                .booker(booker)
                .start(now.plusMinutes(1L))
                .end(now.plusHours(1L)).build();
        bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .booker(userMapper.toDto(booker))
                .start(now.plusMinutes(1L))
                .end(now.plusHours(1L)).build();
        bookings = new ArrayList<>();
        bookings.add(booking);
    }

    @Test
    void createBookingWithValidInputsShouldReturnsBooking() {
        when(userRepository.findById(any())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(booking.toBuilder().id(1L).build());
        when(bookingMapper.fromDto(any())).thenReturn(booking);


        Booking result = bookingService.createBooking(bookingDto, 1L);
        bookingDto.setId(1L);
        booking.setId(1L);

        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getStatus(), BookingStatus.WAITING);
    }

    @Test
    void createBookingWithOwnerBookItemShouldThrowsNotFoundException() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.createBooking(bookingDto, 2L)
        );

        assertEquals("Ты владелец", exception.getMessage());
    }

    @Test
    void createBookingWithNotFoundUserShouldThrowsNotFoundExceptions() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.createBooking(bookingDto, 2L)
        );

        assertEquals("User with ID: 2 not found", exception.getMessage());
    }

    @Test
    void createBookingWithNotFoundItemShouldThrowsNotFoundExceptions() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.createBooking(bookingDto, 2L)
        );

        assertEquals("Элемент с идентификатором:1 не найден", exception.getMessage());
    }

    @Test
    void createBookingWithNotAvailableItemShouldThrowsNotFoundExceptions() {
        item.setAvailable(false);
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        NotAvailableException exception = assertThrows(
                NotAvailableException.class,
                () -> bookingService.createBooking(bookingDto, 2L)
        );

        assertEquals(MessageFormat.format("Элемент с идентификатором: {0} недоступен", item.getId()), exception.getMessage());
    }

    @Test
    void updateBookingStatusWithValidInputsShouldReturnsBookingWithStatusApproved() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(
                booking.toBuilder()
                        .id(1L)
                        .status(BookingStatus.APPROVED).build()
        );
        booking.setStatus(BookingStatus.WAITING);
        Booking result = bookingService.updateBookingStatus(1L, true, 2L);
        booking.setId(1L);

        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getStatus(), result.getStatus());
    }

    @Test
    void updateBookingStatusWithValidInputsShouldReturnsBookingWithStatusRejected() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(
                booking.toBuilder()
                        .id(1L)
                        .status(BookingStatus.REJECTED).build()
        );
        booking.setStatus(BookingStatus.WAITING);
        Booking result = bookingService.updateBookingStatus(1L, false, 2L);
        booking.setId(1L);

        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getStatus(), result.getStatus());
    }

//    @Test
//    void updateBookingWithNotFoundBookingShouldThrowsNotFoundExceptions() {
//        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(
//                NotFoundException.class,
//                () -> bookingService.updateBookingStatus(1L, false, 2L)
//        );
//
//        assertEquals("Бронирование: 1 не найдено", exception.getMessage());
//    }

    @Test
    void updateBookingStatusWithNotAvailableReturnsThrowsNotFound() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.updateBookingStatus(1L, false, 1L)
        );

        assertEquals("User with id: 1 do not have available", exception.getMessage());
    }

//    @Test
//    void updateBookingStatusWithStatusNotWaitingShouldThrowNotAvailableException() {
//        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
//
//        booking.setStatus(BookingStatus.REJECTED);
//
//        NotAvailableException exception = assertThrows(
//                NotAvailableException.class,
//                () -> bookingService.updateBookingStatus(1L, false, 2L)
//        );
//
//        assertEquals("Рассмотрение", exception.getMessage());
//    }

//    @Test
//    void getBookingByIdWithUserOwnerShouldReturnsBooking() {
//        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
//
//        booking.setStatus(BookingStatus.APPROVED);
//        Booking result = bookingService.getBookingById(1L, 2L);
//        booking.setId(1L);
//
//        assertEquals(booking, result);
//    }

//    @Test
//    void getBookingByIdWithUserBookerShouldReturnsBooking() {
//        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
//
//        booking.setStatus(BookingStatus.APPROVED);
//        Booking result = bookingService.getBookingById(1L, 1L);
//        booking.setId(1L);
//
//        assertEquals(booking, result);
//    }

//    @Test
//    void getBookingByIdWithUserNotHasAvailableShouldThrowNotFoundException() {
//        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
//
//        NotFoundException exception = assertThrows(
//                NotFoundException.class,
//                () -> bookingService.getBookingById(1L, 3L)
//        );
//
//        assertEquals("User with id: 1 do not have available", exception.getMessage());
//    }

    @Test
    void getBookerBookingsWithStateAllShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByBookerOrderByStartDesc(booker, pageable)).thenReturn(bookingPage);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        List<Booking> result = bookingService.getBookerBookings(booker.getId(), BookingState.ALL, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

    @Test
    void getBookerBookingsWithStateCurrentShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByBookerAndStartBeforeAndEndAfter(any(), any(), any()))
                .thenReturn(bookingPage);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        List<Booking> result = bookingService.getBookerBookings(booker.getId(), BookingState.CURRENT, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

    @Test
    void getBookerBookingsWithStatePastShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByBookerAndEndDateBeforeOrderByStartDesc(any(), any(), any()))
                .thenReturn(bookingPage);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        List<Booking> result = bookingService.getBookerBookings(booker.getId(), BookingState.PAST, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

    @Test
    void getBookerBookingsWithStateFutureShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByBookerAndStartDateAfterOrderByStartDesc(any(), any(), any()))
                .thenReturn(bookingPage);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        List<Booking> result = bookingService.getBookerBookings(booker.getId(), BookingState.FUTURE, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

    @Test
    void getBookerBookingsWithStateWaitingShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByBookerAndStatus(booker, BookingStatus.WAITING, pageable)).thenReturn(bookingPage);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        List<Booking> result = bookingService.getBookerBookings(booker.getId(), BookingState.WAITING, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

    @Test
    void getBookerBookingsWithStateRejectedShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByBookerAndStatus(booker, BookingStatus.REJECTED, pageable)).thenReturn(bookingPage);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        List<Booking> result = bookingService.getBookerBookings(booker.getId(), BookingState.REJECTED, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

//    @Test
//    void getBookerBookingsWithStateUnsupportedShouldThrowBadStatusException() {
//        Page<Booking> bookingPage = new PageImpl<>(bookings);
//        when(bookingRepository.findByBookerOrderByStartDesc(booker, pageable)).thenReturn(bookingPage);
//        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
//
//        BadStateException e = assertThrows(
//                BadStateException.class,
//                () -> bookingService.getBookerBookings(booker.getId(), BookingState.UNSUPPORTED, pageable)
//        );
//
//        assertEquals(e.getMessage(), "Неизвестное состояние");
//    }

    @Test
    void getOwnerBookingsWithStateAllShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByOwnerOrderByStartDesc(owner, pageable)).thenReturn(bookingPage);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        List<Booking> result = bookingService.getOwnerBookings(owner.getId(), BookingState.ALL, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

    @Test
    void getOwnerBookingsWithStateCurrentShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByOwnerAndStartBeforeAndEndAfterOrder(any(), any(), any())).thenReturn(bookingPage);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        List<Booking> result = bookingService.getOwnerBookings(owner.getId(), BookingState.CURRENT, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

    @Test
    void getOwnerBookingsWithStatePastShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByOwnerAndEndDateBeforeOrderByStartDesc(any(), any(), any())).thenReturn(bookingPage);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        List<Booking> result = bookingService.getOwnerBookings(owner.getId(), BookingState.PAST, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

    @Test
    void getOwnerBookingsWithStateFutureShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByOwnerAndStartDateAfterOrderByStartDesc(any(), any(), any()))
                .thenReturn(bookingPage);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        List<Booking> result = bookingService.getOwnerBookings(owner.getId(), BookingState.FUTURE, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

    @Test
    void getOwnerBookingsWithStateWaitingShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByOwnerAndStatus(owner, BookingStatus.WAITING, pageable)).thenReturn(bookingPage);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        List<Booking> result = bookingService.getOwnerBookings(owner.getId(), BookingState.WAITING, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

    @Test
    void getOwnerBookingsWithStateRejectedShouldReturnBookingList() {
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findByOwnerAndStatus(owner, BookingStatus.REJECTED, pageable)).thenReturn(bookingPage);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        List<Booking> result = bookingService.getOwnerBookings(owner.getId(), BookingState.REJECTED, pageable);

        assertEquals(result.get(0), booking);
        assertEquals(result, bookings);
        assertEquals(result.size(), 1);
    }

//    @Test
//    void getOwnerBookingsWithStateUnsupportedShouldReturnBookingList() {
//        Page<Booking> bookingPage = new PageImpl<>(bookings);
//        when(bookingRepository.findByOwnerAndStatus(owner, BookingStatus.REJECTED, pageable)).thenReturn(bookingPage);
//        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
//
//        BadStateException e = assertThrows(BadStateException.class,
//                () -> bookingService.getOwnerBookings(owner.getId(), BookingState.UNSUPPORTED, pageable)
//        );
//        assertEquals(e.getMessage(), "Неизвестное состояние");
//    }
}
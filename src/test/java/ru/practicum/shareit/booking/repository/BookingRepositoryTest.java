package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.TestDataInitializer;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestDataInitializer.class)
class BookingRepositoryTest {
    private final Pageable pageable = PageRequest.of(0, 10);
    private final LocalDateTime now = LocalDateTime.now();
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestDataInitializer testData;

    @Test
    void findByBookerOrderByStartDesc() {
        List<Booking> result = bookingRepository.findByBookerOrderByStartDesc(testData.getDen(), pageable).toList();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.REJECTED);
        assertThat(result.get(1).getId()).isEqualTo(3L);
        assertThat(result.get(1).getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(result.get(2).getId()).isEqualTo(1L);
        assertThat(result.get(2).getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void findByBookerAndStartDateAfterOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findByBookerAndStartDateAfterOrderByStartDesc(testData.getVik(), now, pageable).toList();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(4L);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void findByBookerAndEndDateBeforeOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findByBookerAndEndDateBeforeOrderByStartDesc(testData.getDen(), now, pageable).toList();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.APPROVED);
    }


    @Test
    void findByBookerAndStartBeforeAndEndAfter() {
        List<Booking> result = bookingRepository
                .findByBookerAndStartBeforeAndEndAfter(testData.getDen(), now, pageable).toList();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.REJECTED);
        assertThat(result.get(1).getId()).isEqualTo(3L);
        assertThat(result.get(1).getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void findByBookerAndStatus() {
        List<Booking> result = bookingRepository
                .findByBookerAndStatus(testData.getDen(), BookingStatus.APPROVED, pageable).toList();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(3L);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(result.get(1).getId()).isEqualTo(1L);
        assertThat(result.get(1).getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void findByOwnerOrderByStartDesc() {
        List<Booking> result = bookingRepository.findByOwnerOrderByStartDesc(testData.getVik(), pageable).toList();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.REJECTED);
        assertThat(result.get(1).getId()).isEqualTo(3L);
        assertThat(result.get(1).getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(result.get(2).getId()).isEqualTo(1L);
        assertThat(result.get(2).getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void findByOwnerAndStartDateAfterOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findByOwnerAndStartDateAfterOrderByStartDesc(testData.getDen(), now, pageable).toList();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(4L);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void findByOwnerAndEndDateBeforeOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findByOwnerAndEndDateBeforeOrderByStartDesc(testData.getVik(), now, pageable).toList();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void findByOwnerAndStartBeforeAndEndAfterOrder() {
        List<Booking> result = bookingRepository
                .findByOwnerAndStartBeforeAndEndAfterOrder(testData.getVik(), now, pageable).toList();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.REJECTED);
        assertThat(result.get(1).getId()).isEqualTo(3L);
        assertThat(result.get(1).getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void findByOwnerAndStatus() {
        List<Booking> result = bookingRepository
                .findByOwnerAndStatus(testData.getVik(), BookingStatus.APPROVED, pageable).toList();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(3L);
        assertThat(result.get(0).getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(result.get(1).getId()).isEqualTo(1L);
        assertThat(result.get(1).getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void findFirstByItemAndStartAfterAndStatusNotOrderByStart() {
        Optional<Booking> result = bookingRepository
                .findFirstByItemAndStartAfterAndStatusOrderByStart(testData.getItem2(), now.minusHours(2L),
                        BookingStatus.APPROVED);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(3L);
        assertThat(result.get().getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void findFirstByItemAndStartBeforeAndStatusNotOrderByStartDesc() {
        Optional<Booking> result = bookingRepository
                .findFirstByItemAndStartLessThanEqualAndStatusOrderByStartDesc(testData.getItem1(), now, BookingStatus.APPROVED);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc() {
        Optional<Booking> result = bookingRepository
                .findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc(testData.getItem1(),
                        testData.getDen(), now, BookingStatus.APPROVED);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getStatus()).isEqualTo(BookingStatus.APPROVED);
    }
}
package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.repository.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT booking FROM Booking booking JOIN FETCH booking.item " +
            "WHERE booking.booker = ?1 ORDER BY booking.start DESC",
            countQuery = "SELECT count(booking) FROM Booking booking JOIN booking.item " +
                    "WHERE booking.booker = ?1")
    Page<Booking> findByBookerOrderByStartDesc(User user, Pageable pageable);

    @Query(value = "SELECT booking FROM Booking booking JOIN FETCH booking.item " +
            "WHERE booking.booker = ?1 AND booking.start > ?2 ORDER BY booking.start DESC",
            countQuery = "SELECT count(booking) FROM Booking booking JOIN booking.item " +
                    "WHERE booking.booker = ?1 AND booking.start > ?2")
    Page<Booking> findByBookerAndStartDateAfterOrderByStartDesc(User booker, LocalDateTime now, Pageable pageable);

    @Query(value = "SELECT booking FROM Booking booking JOIN FETCH booking.item " +
            "WHERE booking.booker = ?1 AND booking.end <= ?2 ORDER BY booking.start DESC",
            countQuery = "SELECT count(booking) FROM Booking booking JOIN booking.item " +
                    "WHERE booking.booker = ?1 AND booking.end <= ?2")
    Page<Booking> findByBookerAndEndDateBeforeOrderByStartDesc(User booker, LocalDateTime now, Pageable pageable);

    @Query(value = "SELECT booking FROM Booking booking JOIN FETCH booking.item " +
            "WHERE booking.booker = ?1 AND booking.start <= ?2 AND booking.end >= ?2",
            countQuery = "SELECT count(booking) FROM Booking booking JOIN booking.item " +
                    "WHERE booking.booker = ?1 AND booking.start <= ?2 AND booking.end >= ?2")
    Page<Booking> findByBookerAndStartBeforeAndEndAfter(User booker, LocalDateTime now, Pageable pageable);

    @Query(value = "SELECT booking FROM Booking booking JOIN FETCH booking.item " +
            "WHERE booking.booker = ?1 AND booking.status like ?2 ORDER BY booking.start DESC",
            countQuery = "SELECT count(booking) FROM Booking booking JOIN booking.item " +
                    "WHERE booking.booker = ?1 AND booking.status like ?2")
    Page<Booking> findByBookerAndStatus(User booker, BookingStatus status, Pageable pageable);

    @Query(value = "SELECT booking FROM Booking booking JOIN FETCH booking.item item JOIN FETCH booking.booker " +
            "WHERE item.owner = ?1 ORDER BY booking.start DESC",
            countQuery = "SELECT count(booking) FROM Booking booking JOIN booking.item item JOIN booking.booker " +
                    "WHERE item.owner = ?1")
    Page<Booking> findByOwnerOrderByStartDesc(User owner, Pageable pageable);

    @Query(value = "SELECT booking FROM Booking booking JOIN FETCH booking.item item JOIN FETCH booking.booker " +
            "WHERE item.owner = ?1 AND booking.start > ?2 ORDER BY booking.start DESC",
            countQuery = "SELECT count(booking) FROM Booking booking JOIN booking.item item JOIN booking.booker " +
                    "WHERE item.owner = ?1 AND booking.start > ?2")
    Page<Booking> findByOwnerAndStartDateAfterOrderByStartDesc(User booker, LocalDateTime now, Pageable pageable);

    @Query(value = "SELECT booking FROM Booking booking JOIN FETCH booking.item item JOIN FETCH booking.booker " +
            "WHERE item.owner = ?1 AND booking.end <= ?2 ORDER BY booking.start DESC",
            countQuery = "SELECT count(booking) FROM Booking booking JOIN booking.item item JOIN booking.booker " +
                    "WHERE item.owner = ?1 AND booking.end <= ?2")
    Page<Booking> findByOwnerAndEndDateBeforeOrderByStartDesc(User booker, LocalDateTime now, Pageable pageable);

    @Query(value = "SELECT booking FROM Booking booking JOIN FETCH booking.item item JOIN FETCH booking.booker " +
            "WHERE item.owner = ?1 AND booking.start <= ?2 AND booking.end >= ?2 ",
            countQuery = "SELECT count(booking) FROM Booking booking JOIN booking.item item JOIN booking.booker " +
                    "WHERE item.owner = ?1 AND booking.start <= ?2 AND booking.end >= ?2")
    Page<Booking> findByOwnerAndStartBeforeAndEndAfterOrder(User booker, LocalDateTime now, Pageable pageable);

    @Query(value = "SELECT booking FROM Booking booking JOIN FETCH booking.item item JOIN FETCH booking.booker " +
            "WHERE item.owner = ?1 AND booking.status like ?2 ORDER BY booking.start DESC",
            countQuery = "SELECT count(booking) FROM Booking booking JOIN booking.item item JOIN booking.booker " +
                    "WHERE item.owner = ?1 AND booking.status like ?2")
    Page<Booking> findByOwnerAndStatus(User booker, BookingStatus status, Pageable pageable);

    Optional<Booking> findFirstByItemAndStartAfterAndStatusOrderByStart(Item item, LocalDateTime now,
                                                                        BookingStatus status);

    Optional<Booking> findFirstByItemAndStartLessThanEqualAndStatusOrderByStartDesc(Item item, LocalDateTime now,
                                                                                    BookingStatus status);

    Optional<Booking> findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc(Item i, User u,
                                                                                      LocalDateTime n, BookingStatus s);

    List<Booking> findFirstByItemInAndStartAfterAndStatusOrderByStart(List<Item> item, LocalDateTime now,
                                                                      BookingStatus status);

    List<Booking> findFirstByItemInAndStartLessThanEqualAndStatusOrderByStartDesc(List<Item> item, LocalDateTime now,
                                                                                  BookingStatus status);
}

package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT booking FROM Booking booking JOIN FETCH booking.item " +
            "WHERE booking.booker = ?1 ORDER BY booking.start DESC")
    List<Booking> findByBookerOrderByStartDesc(User user);

    @Query("SELECT booking FROM Booking booking JOIN FETCH booking.item " +
            "WHERE booking.booker = ?1 AND booking.start > ?2 ORDER BY booking.start DESC")
    List<Booking> findByBookerAndStartDateAfterOrderByStartDesc(User booker, LocalDateTime now);

    @Query("SELECT booking FROM Booking booking JOIN FETCH booking.item " +
            "WHERE booking.booker = ?1 AND booking.end <= ?2 ORDER BY booking.start DESC")
    List<Booking> findByBookerAndEndDateBeforeOrderByStartDesc(User booker, LocalDateTime now);

    @Query("SELECT booking FROM Booking booking JOIN FETCH booking.item " +
            "WHERE booking.booker = ?1 AND booking.start <= ?2 AND booking.end >= ?2 " +
            "ORDER BY booking.start DESC")
    List<Booking> findByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User booker, LocalDateTime now);

    @Query("SELECT booking FROM Booking booking JOIN FETCH booking.item " +
            "WHERE booking.booker = ?1 AND booking.status like ?2 ORDER BY booking.start DESC")
    List<Booking> findByBookerAndStatus(User booker, BookingStatus status);

    @Query("SELECT booking FROM Booking booking JOIN FETCH booking.item item JOIN FETCH booking.booker " +
            "WHERE item.owner = ?1 ORDER BY booking.start DESC")
    List<Booking> findByOwnerOrderByStartDesc(User owner);

    @Query("SELECT booking FROM Booking booking JOIN FETCH booking.item item JOIN FETCH booking.booker " +
            "WHERE item.owner = ?1 AND booking.start > ?2 ORDER BY booking.start DESC")
    List<Booking> findByOwnerAndStartDateAfterOrderByStartDesc(User booker, LocalDateTime now);

    @Query("SELECT booking FROM Booking booking JOIN FETCH booking.item item JOIN FETCH booking.booker " +
            "WHERE item.owner = ?1 AND booking.end <= ?2 ORDER BY booking.start DESC")
    List<Booking> findByOwnerAndEndDateBeforeOrderByStartDesc(User booker, LocalDateTime now);

    @Query("SELECT booking FROM Booking booking JOIN FETCH booking.item item JOIN FETCH booking.booker " +
            "WHERE item.owner = ?1 AND booking.start <= ?2 AND booking.end >= ?2 " +
            "ORDER BY booking.start DESC")
    List<Booking> findByOwnerAndStartBeforeAndEndAfterOrderByStartDesc(User booker, LocalDateTime now);

    @Query("SELECT booking FROM Booking booking JOIN FETCH booking.item item JOIN FETCH booking.booker " +
            "WHERE item.owner = ?1 AND booking.status like ?2 ORDER BY booking.start DESC")
    List<Booking> findByOwnerAndStatus(User booker, BookingStatus status);

    Optional<Booking> findFirstByItemAndStartAfterAndStatusNotOrderByStart(Item item, LocalDateTime now,
                                                                           BookingStatus status);

    Optional<Booking> findFirstByItemAndStartBeforeAndStatusNotOrderByStartDesc(Item item, LocalDateTime now,
                                                                                BookingStatus status);

    Optional<Booking> findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc(Item i, User u,
                                                                                      LocalDateTime n, BookingStatus s);
}

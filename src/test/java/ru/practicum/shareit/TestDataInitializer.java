package ru.practicum.shareit;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.repository.model.Comment;
import ru.practicum.shareit.item.repository.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;

@Getter
@Component
public class TestDataInitializer {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private User vik;
    private User den;
    private Item item1;
    private Item item2;
    private Item item3;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private Booking booking4;
    private Request request1;
    private Request request2;
    private Request request3;

    @PostConstruct
    public void init() {
        LocalDateTime now = LocalDateTime.now();

        createUser();
        createRequest(now);

        createItem();

        createComment(now);

        createBooking(now);
    }

    private void createBooking(LocalDateTime now) {
        booking1 = Booking.builder()
                .status(BookingStatus.APPROVED)
                .booker(den)
                .item(item1)
                .start(now.minusHours(4L))
                .end(now.minusHours(2L)).build();
        booking2 = Booking.builder()
                .status(BookingStatus.REJECTED)
                .booker(den)
                .item(item1)
                .start(now.minusHours(1L))
                .end(now.plusHours(1L)).build();
        booking3 = Booking.builder()
                .status(BookingStatus.APPROVED)
                .booker(den)
                .item(item2)
                .start(now.minusHours(1L))
                .end(now.plusHours(1L)).build();
        booking4 = Booking.builder()
                .status(BookingStatus.WAITING)
                .booker(vik)
                .item(item3)
                .start(now.plusHours(2L))
                .end(now.plusHours(3L)).build();
        bookingRepository.saveAll(Arrays.asList(booking1, booking2, booking3, booking4));
    }

    private void createComment(LocalDateTime now) {
        Comment comment = Comment.builder()
                .text("fine")
                .author(vik)
                .item(item3)
                .created(now).build();
        commentRepository.save(comment);
    }

    private void createItem() {
        item1 = Item.builder()
                .owner(vik)
                .name("аккумуляторная дрель")
                .available(true)
                .description("имеет ударный режим").build();
        item2 = Item.builder()
                .owner(vik)
                .name("электрическая отвертка")
                .available(true)
                .description("аккумулятор держит заряд 20 часов").build();
        item3 = Item.builder()
                .owner(den)
                .name("siphon")
                .description("Газирует")
                .available(true)
                .requestId(request3.getId()).build();
        itemRepository.saveAll(Arrays.asList(item1, item2, item3));
    }

    private void createRequest(LocalDateTime now) {
        request1 = Request.builder()
                .description("I need cucumber. Den")
                .created(now)
                .requester(den).build();
        request2 = Request.builder()
                .description("I need tomato. Den")
                .created(now)
                .requester(den).build();
        request3 = Request.builder()
                .description("I need siphon. Vik")
                .created(now)
                .requester(vik).build();
        requestRepository.saveAll(Arrays.asList(request1, request2, request3));
    }

    private void createUser() {
        vik = User.builder()
                .email("vik@gmail.com")
                .name("vik").build();
        den = User.builder()
                .email("long@gmail.com")
                .name("den").build();
        userRepository.saveAll(Arrays.asList(vik, den));
    }
}
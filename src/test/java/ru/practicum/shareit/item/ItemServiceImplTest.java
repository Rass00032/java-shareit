package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.model.Comment;
import ru.practicum.shareit.item.repository.model.ExtendItem;
import ru.practicum.shareit.item.repository.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ItemServiceImplTest {

    private final Pageable pageable = Pageable.unpaged();
    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;

    private User booker;
    private User owner;
    private Item item;
    private List<Item> items;
    private Comment comment;
    private List<Comment> comments;

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
        items = new ArrayList<>();
        items.add(item);

        comment = Comment.builder().id(1L).author(booker).text("cool").item(item).build();
        comments = new ArrayList<>();
        comments.add(comment);
    }

    @Test
    void createItemWithValidInputsShouldReturnsItem() {
        when(userRepository.findById(any())).thenReturn(Optional.of(owner));
        when(itemRepository.save(any()))
                .thenReturn(item.toBuilder()
                        .id(1L)
                        .name(item.getName())
                        .description(item.getDescription())
                        .build());

        Item result = itemService.add(item, 1L);
        item.setId(1L);

        assertEquals(result.getId(), item.getId());
        assertEquals(result.getName(), item.getName());
        assertEquals(result.getDescription(), item.getDescription());
    }

    @Test
    void createItemWithNonExistingUserShouldThrowsNotFoundException() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.add(item, 3L)
        );

        assertEquals("User with ID: 3 not found", exception.getMessage());
    }

    @Test
    void editWithValidInputsShouldReturnsItem() {
        Item updateItem = Item.builder()
                .id(1L)
                .name("Turbo grill")
                .description("Hottest grill")
                .owner(booker)
                .available(false).build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).then(invocation -> invocation.getArgument(0));

        Item result = itemService.edit(updateItem, 1L, 2L);

        assertEquals(1, result.getId());
        assertEquals("Turbo grill", result.getName());
        assertEquals("Hottest grill", result.getDescription());
        assertFalse(result.getAvailable());
    }

    @Test
    void editWithValidInputsShouldUpdateName() {
        Item updateItem = Item.builder()
                .name("rrr").build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).then(invocation -> invocation.getArgument(0));

        Item result = itemService.edit(updateItem, 1L, 2L);

        assertEquals(1, result.getId());
        assertEquals("rrr", result.getName());
        assertEquals("Hot grill", result.getDescription());
    }

    @Test
    void editWithValidInputsShouldUpdateDescription() {
        Item updateItem = Item.builder()
                .description("rrr").build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).then(invocation -> invocation.getArgument(0));

        Item result = itemService.edit(updateItem, 1L, 2L);

        assertEquals(1, result.getId());
        assertEquals("Grill", result.getName());
        assertEquals("rrr", result.getDescription());
    }

    @Test
    void editWithValidInputsShouldUpdateAvailable() {
        Item updateItem = Item.builder()
                .available(false).build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).then(invocation -> invocation.getArgument(0));

        Item result = itemService.edit(updateItem, 1L, 2L);

        assertEquals(1, result.getId());
        assertEquals("Grill", result.getName());
        assertEquals("Hot grill", result.getDescription());
        assertFalse(result.getAvailable());
    }

    @Test
    void editWithValidInputsDoNotShouldUpdate() {
        Item updateItem = item.toBuilder().build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).then(invocation -> invocation.getArgument(0));

        Item result = itemService.edit(updateItem, 1L, 2L);

        assertEquals(1, result.getId());
        assertEquals("Grill", result.getName());
        assertEquals("Hot grill", result.getDescription());
        assertTrue(result.getAvailable());
    }

    @Test
    void editWithNonEixtItemShouldThrowsNotFoundException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.edit(item, 3L, 1L)
        );

        assertEquals("Вещь не найдена", exception.getMessage());
    }

    @Test
    void editWithNonExistingUserShouldThrowsNotFoundException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.edit(item, 1L, 1L)
        );

        assertEquals("Пользователь не является владельцем элемента", exception.getMessage());
    }


    @Test
    void getItemByIdWithValidInputsShouldReturnsItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(item.getId())).thenReturn(comments);

        ExtendItem result = itemService.getById(1L, 1L);

        assertEquals(result.getId(), item.getId());
        assertEquals(result.getName(), item.getName());
        assertEquals(result.getDescription(), item.getDescription());
        assertEquals(result.getComments(), comments);
    }

    @Test
    void getItemByIdWithValidInputsShouldReturnsItemWithBookings() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(item.getId())).thenReturn(comments);
        when(bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStart(any(), any(), any()))
                .thenReturn(Optional.of(Booking.builder().id(1L).build()));
        when(bookingRepository.findFirstByItemAndStartLessThanEqualAndStatusOrderByStartDesc(any(), any(), any()))
                .thenReturn(Optional.of(Booking.builder().id(2L).build()));

        ExtendItem result = itemService.getById(1L, 2L);

        assertEquals(result.getId(), item.getId());
        assertEquals(result.getName(), item.getName());
        assertEquals(result.getDescription(), item.getDescription());
        assertEquals(result.getComments(), comments);
        assertEquals(result.getNextBooking().getId(), 1L);
        assertEquals(result.getLastBooking().getId(), 2L);
    }

    @Test
    void getAllByUserIdWithValidInputsShouldReturnsItemsWithBookings() {
        List<Booking> nextBookings = new ArrayList<>();
        nextBookings.add(Booking.builder().id(1L).item(item).build());
        List<Booking> lastBookings = new ArrayList<>();
        lastBookings.add(Booking.builder().id(2L).item(item).build());
        Page<Item> itemsPage = new PageImpl<>(items);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findAllByOwnerOrderByIdAsc(any(), any())).thenReturn(itemsPage);
        when(commentRepository.findByItemIn(any(), any())).thenReturn(comments);
        when(bookingRepository.findFirstByItemInAndStartLessThanEqualAndStatusOrderByStartDesc(any(), any(), any()))
                .thenReturn(lastBookings);
        when(bookingRepository.findFirstByItemInAndStartAfterAndStatusOrderByStart(any(), any(), any()))
                .thenReturn(nextBookings);

        List<ExtendItem> result = itemService.getAllByUserId(2L, pageable);

        assertEquals(result.get(0).getId(), item.getId());
        assertEquals(result.get(0).getName(), item.getName());
        assertEquals(result.get(0).getDescription(), item.getDescription());
        assertEquals(result.get(0).getComments(), comments);
        assertEquals(result.get(0).getNextBooking().getId(), 1L);
        assertEquals(result.get(0).getLastBooking().getId(), 2L);
    }

    @Test
    void searchItemsShouldReturnsItems() {
        Page<Item> itemsPage = new PageImpl<>(items);
        when(itemRepository.findLikingByNameOrDescription(any(), any())).thenReturn(itemsPage);
        when(commentRepository.findByItemId(item.getId())).thenReturn(comments);

        List<Item> result = itemService.searchItems("hot", pageable);

        assertEquals(result.get(0).getId(), item.getId());
        assertEquals(result.get(0).getName(), item.getName());
        assertEquals(result.get(0).getDescription(), item.getDescription());
    }

    @Test
    void createCommentWithValidInputsShouldReturnsItem() {
        when(userRepository.findById(any())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc(any(), any(), any(), any()))
                .thenReturn(Optional.of(Booking.builder().build()));
        when(commentRepository.save(any())).thenAnswer(invocation -> {
            Comment saveComment = invocation.getArgument(0);
            saveComment.setId(1L);
            return saveComment;
        });

        Comment result = itemService.addComment(item.getId(), owner.getId(), comment.getText());

        assertEquals(result.getId(), comment.getId());
        assertEquals(result.getText(), comment.getText());
    }

    @Test
    void createCommentWithNonExistingUserShouldThrowsNotFoundException() {
        when(bookingRepository.findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc(any(), any(), any(), any()))
                .thenReturn(Optional.empty());
        when(userRepository.findById(any())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));

        NotAvailableException exception = assertThrows(
                NotAvailableException.class,
                () -> itemService.addComment(item.getId(), owner.getId(), "3L")
        );

        assertEquals(exception.getMessage(),
                MessageFormat.format("Нет доступного бронирования для идентификатора пользователя: {0} и вещи: {1}",
                        item.getId(), owner.getId()));
    }
}
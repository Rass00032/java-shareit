package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto add(long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Variables.USER_WITH_ID_NOT_FOUND, userId));
        Item item = itemMapper.fromDto(itemDto, user);
        Item savedItem = itemRepository.save(item);
        return itemMapper.toDto(savedItem);
    }

    @Override
    public ItemDto edit(long itemId, long userId, ItemDto itemDto) {
        boolean isUpdated = false;
        Item item = getItemOrThrowNotFoundException(itemId);
        log.info("Обновление: {}", item);
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Пользователь не является владельцем элемента");
        }
        Item updatedItem = itemMapper.fromDto(itemDto, item.getOwner());
        String updatedItemName = updatedItem.getName();
        if (updatedItemName != null && !updatedItemName.equals(item.getName())) {
            item.setName(updatedItemName);
            log.info("Название обновлено");
            isUpdated = true;
        }
        String updatedItemDescription = updatedItem.getDescription();
        if (updatedItemDescription != null && !item.getDescription().equals(updatedItemDescription)) {
            item.setDescription(updatedItemDescription);
            log.info("Описание обновлено");
            isUpdated = true;
        }
        if (updatedItem.getAvailable() != null && item.getAvailable() != updatedItem.getAvailable()) {
            item.setAvailable(updatedItem.getAvailable());
            log.info("Доступность обновлена");
            isUpdated = true;
        }
        if (isUpdated) {
            itemRepository.save(item);
        }
        return itemMapper.toDto(item);
    }

    @Override
    public OwnerItemDto getById(long itemId, long userId) {
        Item item = getItemOrThrowNotFoundException(itemId);
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = null;
        Booking nextBooking = null;
        List<Comment> comments = commentRepository.findByItemId(itemId);
        if (item.getOwner().getId() == userId) {
            nextBooking = bookingRepository
                    .findFirstByItemAndStartAfterAndStatusNotOrderByStart(item, now, BookingStatus.REJECTED)
                    .orElse(null);

            lastBooking = bookingRepository
                    .findFirstByItemAndStartBeforeAndStatusNotOrderByStartDesc(item, now, BookingStatus.REJECTED)
                    .orElse(null);
        }
        return itemMapper.toDtoOwner(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<OwnerItemDto> getAllByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(Variables.USER_WITH_ID_NOT_FOUND, userId));

        List<Item> items = itemRepository.findAllByOwner(user);
        LocalDateTime now = LocalDateTime.now();
        return items.stream().map(item -> {
            List<Comment> comments = commentRepository.findByItemId(item.getId());

            Booking nextBooking = bookingRepository
                    .findFirstByItemAndStartAfterAndStatusNotOrderByStart(item, now, BookingStatus.REJECTED)
                    .orElse(null);

            Booking lastBooking = bookingRepository
                    .findFirstByItemAndStartBeforeAndStatusNotOrderByStartDesc(item, now, BookingStatus.REJECTED)
                    .orElse(null);

            return itemMapper.toDtoOwner(item, lastBooking, nextBooking, comments);
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> items = itemRepository.findLikingByNameOrDescription(text);
        return items.stream().map(itemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, String commentText) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(Variables.USER_WITH_ID_NOT_FOUND, userId));

        Item item = getItemOrThrowNotFoundException(itemId);
        LocalDateTime now = LocalDateTime.now();
        Optional<Booking> booking = bookingRepository
                .findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc(item, user, now, BookingStatus.APPROVED);

        if (booking.isEmpty()) {
            throw new NotAvailableException(
                    "Нет доступного бронирования для идентификатора пользователя: {0} и вещи: {1}",
                    userId, itemId);
        }
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(now);
        comment.setItem(item);
        comment.setText(commentText);
        commentRepository.save(comment);
        return itemMapper.toCommentDto(comment);
    }

    private Item getItemOrThrowNotFoundException(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
    }
}
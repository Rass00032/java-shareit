package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(long userId, ItemDto itemDto);

    ItemDto edit(long itemId, long userId, ItemDto itemDto);

    OwnerItemDto getById(long itemId, long userId);

    List<OwnerItemDto> getAllByUserId(long userId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Long userId, Long itemId, String commentText);
}

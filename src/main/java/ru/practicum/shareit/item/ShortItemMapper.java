package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.item.repository.model.Item;

@Mapper(componentModel = "spring")
public interface ShortItemMapper {
    ShortItemDto toShortDto(Item item);
}
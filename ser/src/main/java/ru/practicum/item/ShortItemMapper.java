package ru.practicum.item;

import org.mapstruct.Mapper;
import ru.practicum.item.dto.ShortItemDto;
import ru.practicum.item.model.Item;

@Mapper(componentModel = "spring")
public interface ShortItemMapper {
    ShortItemDto toShortDto(Item item);
}
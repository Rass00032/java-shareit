package ru.practicum.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.booking.mapper.ShortBookingMapper;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.OwnerItemDto;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.ExtendItem;
import ru.practicum.item.model.Item;

@Mapper(componentModel = "spring", uses = {ShortBookingMapper.class})
public interface ItemMapper {

    ItemDto toDto(Item item);


    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "id", source = "itemDto.id")
    @Mapping(target = "name", source = "itemDto.name")
    Item fromDto(ItemDto itemDto);


    @Mapping(target = "id", source = "item.id")
    OwnerItemDto toDtoOwner(ExtendItem item);

    @Mapping(target = "authorName", source = "c.author.name")
    CommentDto toCommentDto(Comment c);
}
package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.mapper.ShortBookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ShortBookingMapper.class})
public interface ItemMapper {

    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    ItemDto toDto(Item item);

    @Mapping(target = "request", ignore = true)
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "id", source = "itemDto.id")
    @Mapping(target = "name", source = "itemDto.name")
    Item fromDto(ItemDto itemDto, User owner);


    @Mapping(target = "comments", source = "commentsList")
    @Mapping(target = "id", source = "item.id")
    OwnerItemDto toDtoOwner(Item item, Booking lastBooking, Booking nextBooking, List<Comment> commentsList);

    @Mapping(target = "authorName", source = "c.author.name")
    CommentDto toCommentDto(Comment c);
}
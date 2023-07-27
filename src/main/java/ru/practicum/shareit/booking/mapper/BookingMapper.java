package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {
    Booking fromDto(BookingDto bookingDto);

    @Mapping(target = "itemId", ignore = true)
    BookingDto toDto(Booking booking);

    @Mapping(target = "itemId", ignore = true)
    @Mapping(target = "id", source = "booking.id")
    BookingDto toDtoWithUser(Booking booking, User booker);
}

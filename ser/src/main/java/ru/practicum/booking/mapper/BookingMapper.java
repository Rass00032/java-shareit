package ru.practicum.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.ItemMapper;
import ru.practicum.user.UserMapper;
import ru.practicum.user.model.User;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {
    Booking fromDto(BookingDto bookingDto);

    @Mapping(target = "itemId", ignore = true)
    BookingDto toDto(Booking booking);

    @Mapping(target = "itemId", ignore = true)
    @Mapping(target = "id", source = "booking.id")
    BookingDto toDtoWithUser(Booking booking, User booker);
}

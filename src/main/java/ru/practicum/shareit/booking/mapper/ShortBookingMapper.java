package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface ShortBookingMapper {
    @Mapping(target = "bookerId", source = "booking.booker.id")
    ShortBookingDto toShortDto(Booking booking);
}
package ru.practicum.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.booking.dto.ShortBookingDto;
import ru.practicum.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface ShortBookingMapper {
    @Mapping(target = "bookerId", source = "booking.booker.id")
    ShortBookingDto toShortDto(Booking booking);
}
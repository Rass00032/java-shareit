package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "requester", ignore = true)
    Request fromDto(RequestDto requestDto);

    RequestDto toDto(Request request);
}

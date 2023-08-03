package ru.practicum.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "requester", ignore = true)
    Request fromDto(RequestDto requestDto);

    RequestDto toDto(Request request);
}

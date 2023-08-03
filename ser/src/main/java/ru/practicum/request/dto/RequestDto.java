package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.practicum.item.dto.ShortItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@Builder()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<ShortItemDto> items;
}
package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.ShortItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    @NotBlank(message = "Описание не может быть пустым", groups = {Create.class})
    @Size(max = 500, groups = {Create.class, Update.class})
    private String description;
    private LocalDateTime created;
    private List<ShortItemDto> items;
}
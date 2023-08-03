package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    private long id;
    @NotBlank(message = "Имя не может быть пустым", groups = {Create.class})
    @Size(max = 255, groups = {Create.class, Update.class})
    private String name;
    @NotBlank(message = "Описание не может быть пустым", groups = {Create.class})
    @Size(max = 512, groups = {Create.class, Update.class})
    private String description;
    @NotNull(message = "Наличие должно быть указано", groups = {Create.class})
    private Boolean available;
    @Positive(message = "Идентификатор запроса не может быть меньше 1", groups = {Create.class})
    private Long requestId;
}

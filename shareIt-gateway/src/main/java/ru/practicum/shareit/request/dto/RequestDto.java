package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.item.dto.ShortItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Accessors(chain = true)
@ToString
@Builder
public class RequestDto {
    private long id;
    @NotBlank(message = "Description can't blank", groups = {Create.class})
    private String description;
    private LocalDateTime created;
    private List<ShortItemDto> items;

    public RequestDto setId(long id) {
        this.id = id;
        return this;
    }

    public RequestDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public RequestDto setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public RequestDto setItems(List<ShortItemDto> items) {
        this.items = items;
        return this;
    }
}

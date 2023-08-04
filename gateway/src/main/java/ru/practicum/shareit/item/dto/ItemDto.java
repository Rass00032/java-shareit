package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    private long id;
    @NotBlank(message = "Name can't blank", groups = {Create.class})
    private String name;
    @NotBlank(message = "Description can't blank", groups = {Create.class})
    private String description;
    @NotNull(message = "Available should be specified", groups = {Create.class})
    private Boolean available;
    @Positive(message = "Request ID can't be less than 1", groups = {Create.class})
    private Long requestId;
}

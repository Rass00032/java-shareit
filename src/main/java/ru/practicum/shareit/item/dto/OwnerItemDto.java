package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@ToString
@Builder
public class OwnerItemDto {
    private long id;
    @NotBlank(message = "Имя не может быть пустым", groups = {Create.class})
    private String name;
    @NotBlank(message = "Описание не может быть пустым", groups = {Create.class})
    private String description;
    @NotNull(message = "Наличие должно быть указано", groups = {Create.class})
    private Boolean available;
    private ShortBookingDto lastBooking;
    private ShortBookingDto nextBooking;
    private List<CommentDto> comments;
}

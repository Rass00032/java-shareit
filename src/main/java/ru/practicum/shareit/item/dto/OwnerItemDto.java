package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class OwnerItemDto {
    private long id;
    @NotBlank(message = "Имя не может быть пустым", groups = {Create.class})
    @Size(max = 255, groups = {Create.class, Update.class})
    private String name;
    @NotBlank(message = "Описание не может быть пустым", groups = {Create.class})
    @Size(max = 512, groups = {Create.class, Update.class})
    private String description;
    @NotNull(message = "Наличие должно быть указано", groups = {Create.class})
    private Boolean available;
    private ShortBookingDto lastBooking;
    private ShortBookingDto nextBooking;
    private List<CommentDto> comments;
}

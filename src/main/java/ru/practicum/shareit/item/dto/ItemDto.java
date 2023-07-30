package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    private long id;
    @NotBlank(message = "Имя не может быть пустым", groups = {Create.class})
    private String name;
    @NotBlank(message = "Описание не может быть пустым", groups = {Create.class})
    private String description;
    @NotNull(message = "Наличие должно быть указано", groups = {Create.class})
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
}

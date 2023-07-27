package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {
    private long id;
    @Positive(groups = Create.class)
    @NotNull(groups = Create.class)
    private Long itemId;
    @FutureOrPresent(groups = Create.class)
    @NotNull(groups = Create.class)
    private LocalDateTime start;
    @Future(groups = Create.class)
    @NotNull(groups = Create.class)
    private LocalDateTime end;
    @NotNull(groups = Update.class)
    private BookingStatus status;
    private UserDto booker;
    private ItemDto item;

    @JsonIgnore
    @AssertTrue(groups = Create.class,
            message = "Дата окончания должна быть после даты начала, и даты не могут быть одинаковыми")
    public boolean isValidDateOrder() {
        if (start == null || end == null) {
            return false;
        }
        return end.isAfter(start) && !end.equals(start);
    }
}

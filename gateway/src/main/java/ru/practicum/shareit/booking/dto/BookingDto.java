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
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

    @JsonIgnore
    @AssertTrue(groups = Create.class,
            message = "End date must be after start date and dates cannot be equal")
    public boolean isValidDateOrder() {
        if (start == null || end == null) {
            return false;
        }
        return end.isAfter(start) && !end.equals(start);
    }
}

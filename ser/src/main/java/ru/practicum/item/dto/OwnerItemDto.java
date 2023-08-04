package ru.practicum.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.booking.dto.ShortBookingDto;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class OwnerItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private ShortBookingDto lastBooking;
    private ShortBookingDto nextBooking;
    private List<CommentDto> comments;
}

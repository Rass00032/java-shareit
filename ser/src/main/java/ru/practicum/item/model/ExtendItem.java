package ru.practicum.item.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.booking.model.Booking;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ExtendItem extends Item {
    private Booking lastBooking;
    private Booking nextBooking;
    private List<Comment> comments;

    public ExtendItem(Item item) {
        this.setId(item.getId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setOwner(item.getOwner())
                .setRequestId(item.getRequestId())
                .setAvailable(item.getAvailable());

    }
}

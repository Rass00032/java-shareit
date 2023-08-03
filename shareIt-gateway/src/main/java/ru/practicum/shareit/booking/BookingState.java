package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.BadStateException;

import java.util.Locale;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static BookingState get(String state) {
        try {
            return BookingState.valueOf(state.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new BadStateException("Unknown state: {0}", state);
        }
    }
}

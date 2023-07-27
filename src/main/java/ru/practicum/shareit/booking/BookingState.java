package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.BadStateException;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static BookingState get(String state) {
        try {
            return BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new BadStateException("Неизвестное состояние: {0}", state);
        }
    }
}

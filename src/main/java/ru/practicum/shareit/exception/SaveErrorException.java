package ru.practicum.shareit.exception;

import java.text.MessageFormat;

public class SaveErrorException extends RuntimeException {
    public SaveErrorException(String message) {
        super(message);
    }

    public SaveErrorException(String message, Object... args) {
        this(MessageFormat.format(message, args));
    }
}

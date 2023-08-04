package ru.practicum.exception;

import java.text.MessageFormat;

public class BadStateException extends RuntimeException {
    public BadStateException(String message) {
        super(message);
    }

    public BadStateException(String message, Object... args) {
        this(MessageFormat.format(message, args));
    }
}

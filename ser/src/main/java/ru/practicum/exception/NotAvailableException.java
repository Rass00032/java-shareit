package ru.practicum.exception;

import java.text.MessageFormat;

public class NotAvailableException extends RuntimeException {
    public NotAvailableException(String message) {
        super(message);
    }

    public NotAvailableException(String message, Object... args) {
        this(MessageFormat.format(message, args));
    }
}

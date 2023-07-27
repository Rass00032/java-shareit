package ru.practicum.shareit.exception;

public class UserFoundException extends RuntimeException {
    public UserFoundException(String message) {
        super(message);
    }
}

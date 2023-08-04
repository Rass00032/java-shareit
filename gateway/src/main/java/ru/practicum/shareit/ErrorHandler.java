package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.exception.BadStateException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwableHandler(final Exception e) {
        log.info("Server error {}", e.getMessage());
        return ErrorResponse.builder().error(e.getMessage()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = (error).getDefaultMessage();
            errors.put(fieldName, errorMessage);
            log.info("Validation failed for field: {}. Error message: {}", fieldName, errorMessage);
        });
        return ErrorResponse.builder().error("Validation failed").errors(errors).build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach((error) -> {
            String fieldName = error.getPropertyPath().toString().split("\\.")[1];
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
            log.info("Validation failed for field: {}. Error message: {}", fieldName, errorMessage);
        });
        return ErrorResponse.builder().error("Validation failed").errors(errors).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(HttpMessageNotReadableException e) {
        log.info("Malformed JSON Request: {}", e.getMessage());
        return ErrorResponse.builder().error("Malformed JSON Request").build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ErrorResponse handleException(MethodArgumentTypeMismatchException e) {
        log.info("Method argument type mismatch: {}", e.getMessage());
        return ErrorResponse.builder().error(e.getMessage()).build();
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(MissingRequestHeaderException e) {
        log.info("Missing request header: {}", e.getMessage());
        return ErrorResponse.builder().error(e.getMessage()).build();
    }

    @ExceptionHandler(BadStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(BadStateException e) {
        log.info("Bad booking state: {}", e.getMessage());
        return ErrorResponse.builder().error(e.getMessage()).build();
    }
}

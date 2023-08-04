package ru.practicum.shareit;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ErrorResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Map<String, String> errors;
    private String error;
}

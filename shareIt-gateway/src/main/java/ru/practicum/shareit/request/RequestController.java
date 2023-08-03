package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/requests")
@Validated
public class RequestController {

    private final RequestClient requestClient;


    // Добавление нового запроса
    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                          @Validated(Create.class) @RequestBody RequestDto requestDto) {
            log.info("Add request on item {}, user id: {}", requestDto, userId);
        return requestClient.createRequest(userId, requestDto);
    }

    // Просмотр информации о конкретном запросе по его идентификатору
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                              @PathVariable @Positive Long requestId) {
        log.info("Request from user ID: {} on get request. ID: {}", userId, requestId);
        return requestClient.getRequest(userId, requestId);
    }

    //получить список запросов, созданных другими пользователями
    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "20") @Positive int size) {
        log.info("Request on get all requests from user id: {}", userId);
        return requestClient.getRequests(userId, from, size);
    }

    // Просмотр пользователем списка всех его запросов с указанием ответов на них
    @GetMapping("/all")
    public ResponseEntity<Object> getRequesterAll(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "20") @Positive int size) {
        log.info("Request on get all requests");

        return requestClient.getRequesterAll(userId, from, size);
    }
}

package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Util;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
@Validated
public class RequestController {

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @PostMapping
    public RequestDto add(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                          @Validated(Create.class) @RequestBody RequestDto requestDto) {
        log.info("Добавить запрос на элемент {}, идентификатор пользователя: {}", requestDto, userId);
        Request request = requestMapper.fromDto(requestDto);
        requestDto = requestMapper.toDto(requestService.add(userId, request));
        return requestDto;
    }

    @GetMapping("/{requestId}")
    public RequestDto getById(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                              @PathVariable @Positive Long requestId) {
        log.info("Запрос от идентификатора пользователя: {} по запросу get. Идентификатор: {}", userId, requestId);
        return requestMapper.toDto(requestService.getById(requestId, userId));
    }

    @GetMapping
    public List<RequestDto> getRequesterAll(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "20") @Positive int size) {
        Pageable pageable = Util.getPageable(from, size);
        log.info("Запрос на получение всех запросов от идентификатора пользователя: {}", userId);
        return requestService.getAllForUser(userId, pageable)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<RequestDto> getAll(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                   @RequestParam(defaultValue = "20") @Positive int size) {
        Pageable pageable = Util.getPageable(from, size);
        List<RequestDto> dtoList = requestService.getAll(userId, pageable)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
        log.info("Запрос на получение всех запросов");
        return dtoList;
    }
}

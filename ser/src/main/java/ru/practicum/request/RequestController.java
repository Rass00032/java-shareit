package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Util;
import ru.practicum.Variables;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

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
    public RequestDto add(@RequestHeader(Variables.USER_ID) Long userId,
                          @RequestBody RequestDto requestDto) {
        log.info("Добавить запрос на элемент {}, идентификатор пользователя: {}", requestDto, userId);
        Request request = requestMapper.fromDto(requestDto);
        requestDto = requestMapper.toDto(requestService.add(userId, request));
        return requestDto;
    }

    @GetMapping("/{requestId}")
    public RequestDto getById(@RequestHeader(Variables.USER_ID) Long userId,
                              @PathVariable Long requestId) {
        log.info("Запрос от идентификатора пользователя: {} по запросу get. Идентификатор: {}", userId, requestId);
        return requestMapper.toDto(requestService.getById(requestId, userId));
    }

    @GetMapping
    public List<RequestDto> getRequesterAll(@RequestHeader(Variables.USER_ID) Long userId,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = Util.getPageable(from, size);
        log.info("Запрос на получение всех запросов от идентификатора пользователя: {}", userId);
        return requestService.getAllForUser(userId, pageable)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<RequestDto> getAll(@RequestHeader(Variables.USER_ID) Long userId,
                                   @RequestParam(defaultValue = "0") int from,
                                   @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = Util.getPageable(from, size);
        List<RequestDto> dtoList = requestService.getAll(userId, pageable)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
        log.info("Запрос на получение всех запросов");
        return dtoList;
    }
}

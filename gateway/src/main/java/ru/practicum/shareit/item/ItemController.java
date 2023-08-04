package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Validated
@RequiredArgsConstructor
@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    // Добавление новой вещи
    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                       @RequestBody @Validated(Create.class) ItemDto itemDto) {
        log.info("Request on add {}, user id: {}", itemDto, userId);

        return itemClient.createItem(userId, itemDto);
    }

    // Редактирование вещи
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> edit(@PathVariable @Positive Long itemId,
                                        @RequestHeader(Variables.USER_ID) @Positive Long userId,
                                        @RequestBody ItemDto itemDto) {
        log.info("Request on edit item id: {}, {}, user id: {}", itemId, itemDto, userId);

        return itemClient.updateItem(itemId, userId, itemDto);
    }

    // Просмотр информации о конкретной вещи по её идентификатору,
    // если смотрит владелец добавить последнее и будущее бронирование
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                @PathVariable @Positive Long itemId) {
        log.info("Request from user ID: {} on get item. ID: {}", userId, itemId);
        return itemClient.getById(itemId, userId);
    }

    // Просмотр владельцем списка всех его вещей с указанием названия и описанием для каждой
    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                     @RequestParam(defaultValue = "20") @Positive int size) {
        log.info("Request on get all for user id: {}", userId);
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return itemClient.getAllUserItems(userId, parameters);
    }

    // Поиск вещи потенциальным арендатором
    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") String text,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                     @RequestParam(defaultValue = "20") @Positive int size) {
        log.info("Request on search text: {}", text);
        if (text.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "text", text.toLowerCase(Locale.ROOT)
        );
        return itemClient.searchItems(parameters);
    }

    // Добавление комментария
    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                 @RequestBody @Validated(Create.class) CommentDto commentDto,
                                                 @PathVariable @Positive Long itemId) {
        log.info("Request on add comment {}, item id: {}, user id: {}", commentDto.getText(), itemId, userId);
        return itemClient.createComment(itemId, userId, commentDto);
    }
}

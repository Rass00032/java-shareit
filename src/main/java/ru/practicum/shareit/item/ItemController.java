package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> add(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                       @RequestBody @Validated(Create.class) ItemDto itemDto) {
        log.info("Запрос на добавление {}, идентификатор пользователя: {}", itemDto, userId);
        return ResponseEntity.ok(itemService.add(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> edit(@PathVariable @Positive Long itemId,
                                        @RequestHeader(Variables.USER_ID) @Positive Long userId,
                                        @RequestBody ItemDto itemDto) {
        log.info("Запрос на редактирование идентификатора элемента: {}, {}, идентификатор пользователя: {}"
                , itemId, itemDto, userId);
        return ResponseEntity.ok(itemService.edit(itemId, userId, itemDto));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<OwnerItemDto> getById(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                @PathVariable @Positive Long itemId) {
        log.info("Запрос от идентификатора пользователя: {} при получении элемента. Идентификатор: {}", userId, itemId);
        return ResponseEntity.ok(itemService.getById(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<OwnerItemDto>> getAll(@RequestHeader(Variables.USER_ID) @Positive Long userId) {
        log.info("Запрос на получение всего для идентификатора пользователя: {}", userId);
        List<OwnerItemDto> allByUserId = itemService.getAllByUserId(userId);
        System.out.println(false);
        return ResponseEntity.ok(allByUserId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam("text") String text) {
        log.info("Запрос по поисковому тексту: {}", text);
        if (text.isBlank()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(itemService.searchItems(text.toLowerCase(Locale.ROOT)));
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                                 @RequestBody @Validated(Create.class) CommentDto commentDto,
                                                 @PathVariable @Positive Long itemId) {
        log.info("Запрос на добавление комментария {}, идентификатор элемента: {}, идентификатор пользователя: {}",
                commentDto.getText(), itemId, userId);
        return ResponseEntity.ok(itemService.addComment(userId, itemId, commentDto.getText()));
    }
}

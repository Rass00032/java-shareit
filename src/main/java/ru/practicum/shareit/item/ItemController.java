package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.repository.model.Comment;
import ru.practicum.shareit.item.repository.model.ExtendItem;
import ru.practicum.shareit.item.repository.model.Item;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemMapper itemMapper;
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                       @RequestBody @Validated(Create.class) ItemDto itemDto) {
        log.info("Запрос на добавление {}, идентификатор пользователя: {}", itemDto, userId);
        Item item = itemService.add(itemMapper.fromDto(itemDto), userId);
        return itemMapper.toDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto edit(@PathVariable @Positive Long itemId,
                        @RequestHeader(Variables.USER_ID) @Positive Long userId,
                        @RequestBody ItemDto itemDto) {
        log.info("Запрос на редактирование идентификатора элемента: {}, {}, идентификатор пользователя: {}",
                itemId, itemDto, userId);
        Item item = itemService.edit(itemMapper.fromDto(itemDto), itemId, userId);
        return itemMapper.toDto(item);
    }

    @GetMapping("/{itemId}")
    public OwnerItemDto getById(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                @PathVariable @Positive Long itemId) {
        log.info("Запрос от идентификатора пользователя: {} при получении элемента. Идентификатор: {}",
                userId, itemId);
        ExtendItem item = itemService.getById(itemId, userId);
        return itemMapper.toDtoOwner(item);
    }

    @GetMapping
    public List<OwnerItemDto> getAll(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(defaultValue = "20") @Positive int size) {
        Pageable pageable = Util.getPageable(from, size);
        log.info("Запрос на получение всего для идентификатора пользователя: {}", userId);
        List<ExtendItem> allByUserId = itemService.getAllByUserId(userId, pageable);
        return allByUserId.stream()
                .map(itemMapper::toDtoOwner)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String text,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(defaultValue = "20") @Positive int size) {
        Pageable pageable = Util.getPageable(from, size);
        log.info("Запрос по поисковому тексту: {}", text);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> items = itemService.searchItems(text.toLowerCase(Locale.ROOT), pageable);
        return items.stream().map(itemMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@RequestHeader(Variables.USER_ID) @Positive Long userId,
                                 @RequestBody @Validated(Create.class) CommentDto commentDto,
                                 @PathVariable @Positive Long itemId) {
        log.info("Запрос на добавление комментария {}, идентификатор элемента: {}, идентификатор пользователя: {}",
                commentDto.getText(), itemId, userId);
        Comment comment = itemService.addComment(userId, itemId, commentDto.getText());
        return itemMapper.toCommentDto(comment);
    }
}

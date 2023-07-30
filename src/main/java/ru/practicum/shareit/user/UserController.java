package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("Запрос на получение всего");
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getById(@PathVariable @Positive(message = "ID должен быть положительным") Long id) {
        log.info("Запрос на получение пользователя ID: {}", id);
        return ResponseEntity.ok(userService.getById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ResponseEntity<UserDto> add(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.debug("Запрос на создание: {}", userDto);
        UserDto newUser = userService.add(userDto);
        log.info("Было создано: {}", newUser);
        return ResponseEntity.ok(newUser);
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserDto> edit(@Validated(Update.class) @RequestBody UserDto userDto,
                                        @PathVariable @Positive(message = "ID должно быть положительным") Long id) {
        log.debug("Запрос на обновление: {}", userDto);
        UserDto updatedUser = userService.edit(id, userDto);
        log.info("Был обновлен: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable @Positive(message = "ID должно быть положительным") Long id) {
        log.info("Запрос на удаление с ID: {}", id);
        userService.delete(id);
    }
}

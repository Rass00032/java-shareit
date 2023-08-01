package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("Запрос на получение всего");
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("{id}")
    public UserDto getById(@PathVariable @Positive(message = "ID должен быть положительным") Long id) {
        log.info("Запрос на получение пользователя ID: {}", id);
        return userMapper.toDto(userService.getById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto add(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.debug("Запрос на создание: {}", userDto);
        User newUser = userService.add(userDto);
        log.info("Было создано: {}", newUser);
        return userMapper.toDto(newUser);
    }

    @PatchMapping("{id}")
    public UserDto edit(@Validated(Update.class) @RequestBody UserDto userDto,
                                        @PathVariable @Positive(message = "ID должно быть положительным") Long id) {
        log.debug("Запрос на обновление: {}", userDto);
        User updatedUser = userService.edit(id, userMapper.fromDto(userDto));
        log.info("Был обновлен: {}", updatedUser);
        return userMapper.toDto(updatedUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable @Positive(message = "ID должно быть положительным") Long id) {
        log.info("Запрос на удаление с ID: {}", id);
        userService.delete(id);
    }
}

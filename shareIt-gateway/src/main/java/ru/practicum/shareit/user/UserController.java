package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    //Просмотр всех пользователей.
    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Request on get all");
        return userClient.getUsers();
    }

    //Просмотр пользователя по идентификатору
    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable @Positive(message = "ID should be positive") Long id) {
        log.info("Request to get user with ID: {}", id);
        return userClient.getUser(id);
    }

    //Добавление нового пользователя
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> create(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.debug("Request to create: {}", userDto);
        return userClient.create(userDto);
    }

    //
//    //Редактирование нового пользователя
    @PatchMapping("{id}")
    public ResponseEntity<Object> update(@Validated(Update.class) @RequestBody UserDto userDto,
                                         @PathVariable @Positive(message = "ID should be positive") Long id) {
        log.debug("Request to updated: {}", userDto);
        ResponseEntity<Object> updatedUser = userClient.update(id, userDto);
        log.info("Has been updated: {}", updatedUser);
        return updatedUser;
    }

    //Удаление пользователя
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable @Positive(message = "ID should be positive") Long id) {
        log.info("Request to delete with id: {}", id);
        return userClient.delete(id);
    }
}

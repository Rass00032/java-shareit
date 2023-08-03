package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User add(UserDto userDto);

    User edit(Long id, User user);

    List<UserDto> getAll();

    User getById(Long id);

    void delete(Long id);
}

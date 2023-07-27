package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto add(UserDto userDto);

    UserDto edit(Long id, UserDto userDto);

    List<UserDto> getAll();

    UserDto getById(Long id);

    void delete(Long id);
}

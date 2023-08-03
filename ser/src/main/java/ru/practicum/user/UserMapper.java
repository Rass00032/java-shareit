package ru.practicum.user;

import org.mapstruct.Mapper;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromDto(UserDto userDto);

    UserDto toDto(User user);
}

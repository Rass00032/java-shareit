package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@Setter
@ToString
public class UserDto {

    private long id;

    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9-]+(.[A-Z0-9-]+)*\\.[A-Z]{2,}$", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Неверный адрес электронной почты", groups = {Create.class, Update.class})
    @NotNull(message = "Адрес электронной почты не может быть пустым", groups = {Create.class})
    private String email;

    private String name;
}

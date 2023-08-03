package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Builder
@Getter
@Setter
@ToString
public class UserDto {

    private long id;

    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9-]+(.[A-Z0-9-]+)*\\.[A-Z]{2,}$", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Неверный адрес электронной почты", groups = {Create.class, Update.class})
    @NotNull(message = "Адрес электронной почты не может быть пустым", groups = {Create.class})
    @Size(max = 512, groups = {Create.class, Update.class})
    private String email;

    @Size(max = 255, groups = {Create.class, Update.class})
    private String name;
}

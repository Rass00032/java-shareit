package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class CommentDto {
    private long id;
    @NotBlank(groups = Create.class)
    @Size(max = 500, groups = {Create.class, Update.class})
    private String text;
    private String authorName;
    private LocalDateTime created;
}

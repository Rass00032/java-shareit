package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class CommentDto {
    private long id;
    @NotBlank(groups = Create.class)
    private String text;
    private String authorName;
    private LocalDateTime created;
}

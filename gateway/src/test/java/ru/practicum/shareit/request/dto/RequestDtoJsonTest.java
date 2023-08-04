package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ShortItemDto;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestDtoJsonTest {

    @Autowired
    private JacksonTester<RequestDto> json;

    @Test
    void testSerialize() throws Exception {
        RequestDto requestDto = RequestDto.builder()
                .id(1L)
                .description("Test description")
                .created(LocalDateTime.of(2022, 1, 1, 0, 0))
                .items(Arrays.asList(
                        ShortItemDto.builder()
                                .id(1L)
                                .name("Item 1")
                                .description("Item 1 description")
                                .available(true)
                                .requestId(1L)
                                .build(),
                        ShortItemDto.builder()
                                .id(2L)
                                .name("Item 2")
                                .description("Item 2 description")
                                .available(true)
                                .requestId(1L)
                                .build()
                ))
                .build();

        JsonContent<RequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test description");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2022-01-01T00:00:00");
        assertThat(result).extractingJsonPathArrayValue("$.items[*].id")
                .containsExactly(1, 2);
        assertThat(result).extractingJsonPathArrayValue("$.items[*].name")
                .containsExactly("Item 1", "Item 2");
        assertThat(result).extractingJsonPathArrayValue("$.items[*].description")
                .containsExactly("Item 1 description", "Item 2 description");
        assertThat(result).extractingJsonPathArrayValue("$.items[*].available")
                .containsExactly(true, true);
        assertThat(result).extractingJsonPathArrayValue("$.items[*].requestId")
                .containsExactly(1, 1);
    }
}
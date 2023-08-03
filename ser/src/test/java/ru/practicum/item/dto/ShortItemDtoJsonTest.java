package ru.practicum.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ShortItemDtoJsonTest {

    @Autowired
    private JacksonTester<ShortItemDto> json;

    @Test
    void shouldSerializeToJson() throws Exception {
        ShortItemDto itemDto = ShortItemDto.builder()
                .id(1L)
                .name("Test name")
                .description("Test description")
                .available(true)
                .requestId(123L)
                .build();

        JsonContent<ShortItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(123);
    }

    @Test
    void shouldSerializeToJsonWithNullFields() throws Exception {
        ShortItemDto itemDto = ShortItemDto.builder()
                .id(1L)
                .name("Test name")
                .available(true)
                .build();

        JsonContent<ShortItemDto> result = json.write(itemDto);
        result.assertThat().extractingJsonPathNumberValue("$.id").isEqualTo(1);
        result.assertThat().extractingJsonPathStringValue("$.name").isEqualTo("Test name");
        result.assertThat().doesNotHaveJsonPathValue("$.description");
        result.assertThat().extractingJsonPathBooleanValue("$.available").isTrue();
        result.assertThat().doesNotHaveJsonPathValue("$.requestId");
    }
}
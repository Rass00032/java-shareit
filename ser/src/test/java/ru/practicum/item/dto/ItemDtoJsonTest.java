package ru.practicum.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    public void testSerialize() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Test name")
                .description("Test description")
                .available(true)
                .requestId(123L)
                .build();

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(123);
    }

    @Test
    public void testSerializeWithNullValues() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Test name")
                .available(true)
                .build();

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test name");
        assertThat(result).doesNotHaveJsonPathValue("$.description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).doesNotHaveJsonPathValue("$.requestId");
    }
}
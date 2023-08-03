package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class OwnerItemDtoJsonTest {

    @Autowired
    private JacksonTester<OwnerItemDto> json;

    @Test
    public void testSerialize() throws Exception {
        CommentDto comment1 = CommentDto.builder().id(1).text("Test comment 1").authorName("Test author").created(LocalDateTime.now()).build();
        CommentDto comment2 = CommentDto.builder().id(2).text("Test comment 2").authorName("Test author").created(LocalDateTime.now()).build();
        ShortBookingDto lastBooking = ShortBookingDto.builder().id(1).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(2)).build();
        ShortBookingDto nextBooking = ShortBookingDto.builder().id(2).start(LocalDateTime.now().plusHours(3)).end(LocalDateTime.now().plusHours(5)).build();

        OwnerItemDto ownerItemDto = OwnerItemDto.builder()
                .id(1)
                .name("Test name")
                .description("Test description")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(Arrays.asList(comment1, comment2))
                .build();

        JsonContent<OwnerItemDto> result = json.write(ownerItemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.start").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.end").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.start").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.end").isNotNull();
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("Test comment 1");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo("Test author");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].created").isNotNull();
        assertThat(result).extractingJsonPathNumberValue("$.comments[1].id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.comments[1].text").isEqualTo("Test comment 2");
        assertThat(result).extractingJsonPathStringValue("$.comments[1].authorName").isEqualTo("Test author");
        assertThat(result).extractingJsonPathStringValue("$.comments[1].created").isNotNull();
    }
}

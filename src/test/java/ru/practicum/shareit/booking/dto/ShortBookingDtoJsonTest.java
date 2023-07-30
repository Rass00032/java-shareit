package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ShortBookingDtoJsonTest {

    @Autowired
    private JacksonTester<ShortBookingDto> json;

    @Test
    public void testSerialize() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 5, 11, 14, 0);
        LocalDateTime end = LocalDateTime.of(2023, 5, 11, 16, 0);
        ShortBookingDto bookingDto = ShortBookingDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .bookerId(2L).build();

        JsonContent<ShortBookingDto> jsonContent = json.write(bookingDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo("11-05-2023 14:00:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo("11-05-2023 16:00:00");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.bookerId").isEqualTo(2);
    }

}
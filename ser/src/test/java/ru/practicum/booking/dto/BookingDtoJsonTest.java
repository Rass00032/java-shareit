package ru.practicum.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.booking.BookingStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testCreateUserDto() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .itemId(1L)
                .start(now)
                .end(now.plusHours(1L))
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(
                now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(
                now.plusHours(1L).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).doesNotHaveJsonPath("$.status");
        assertThat(result).doesNotHaveJsonPath("$.booker");
        assertThat(result).doesNotHaveJsonPath("$.item");
        assertThat(result).doesNotHaveJsonPath("$.isValidDateOrder");
    }

    @Test
    void testUpdateUserDto() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .status(BookingStatus.APPROVED)
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).doesNotHaveJsonPath("$.itemId");
        assertThat(result).doesNotHaveJsonPath("$.start");
        assertThat(result).doesNotHaveJsonPath("$.end");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).doesNotHaveJsonPath("$.booker");
        assertThat(result).doesNotHaveJsonPath("$.item");
        assertThat(result).doesNotHaveJsonPath("$.isValidDateOrder");
    }

}
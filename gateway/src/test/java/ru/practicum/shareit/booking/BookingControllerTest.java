package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingClient bookingClient;

    private BookingDto bookingDto;

    @BeforeEach
    public void set() {
        bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusMinutes(2L))
                .end(LocalDateTime.now().plusHours(2L))
                .build();
    }

    @Test
    void createBookingShouldReturnOkWhenValidRequest() throws Exception {
        long userId = 1L;
        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createBookingShouldReturnBadRequestWhenBookingNull() throws Exception {
        long userId = 1L;
        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(null))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Malformed JSON Request")));
    }

    @Test
    void createBookingShouldReturnBadRequestWhenBookingNotValid() throws Exception {
        long userId = 1L;
        UserDto badBooking = UserDto.builder()
                .id(1L)
                .name("booking name")
                .email("email")
                .build();

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(badBooking))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect((jsonPath("$.errors", aMapWithSize(4))));
    }

    @Test
    void createBookingShouldReturnBadRequestWhenUserIdNotValid() throws Exception {
        String userId = "1L";

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Failed to convert value of type")));
    }

    @Test
    void createBookingShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        long userId = 0L;

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.userId", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void createBookingShouldReturnBadRequestWhenBookingStartIsPast() throws Exception {
        long userId = 1L;
        BookingDto badBooking = BookingDto.builder()
                .id(1L)
                .itemId(1L)
                .start(bookingDto.getStart().minusHours(3L))
                .end(bookingDto.getEnd())
                .status(BookingStatus.WAITING)
                .build();

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(badBooking))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.start", is("must be a date in the present or in the future")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void createBookingShouldReturnBadRequestWhenBookingEndIsNowOrPast() throws Exception {
        long userId = 1L;
        BookingDto badBooking = BookingDto.builder()
                .id(1L)
                .itemId(1L)
                .start(bookingDto.getStart())
                .end(LocalDateTime.now().minusHours(3L))
                .status(BookingStatus.WAITING)
                .build();

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(badBooking))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.end", is("must be a future date")))
                .andExpect((jsonPath("$.errors", aMapWithSize(2))));
    }

    @Test
    void createBookingShouldReturnBadRequestWhenItemIdNull() throws Exception {
        long userId = 1L;
        BookingDto badBooking = BookingDto.builder()
                .id(1L)
                .start(bookingDto.getStart())
                .end(LocalDateTime.now().plusHours(3L))
                .status(BookingStatus.WAITING)
                .build();

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(badBooking))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.itemId", is("must not be null")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void updateBookingStatusShouldReturnOkWhenValidRequest() throws Exception {
        long userId = 1L;

        mockMvc.perform(patch("/bookings/{0}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateBookingStatusShouldReturnBadRequestWhenBookingNull() throws Exception {
        long userId = 1L;
        mockMvc.perform(patch("/bookings/{0}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "null")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Failed to convert value of type")));
    }

    @Test
    void getOwnerBookingShouldReturnOkWhenValidRequest() throws Exception {

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getOwnerBookingShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 0L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.userId", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getOwnerBookingShouldReturnBadRequestWhenFromNegative() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .param("from", "-1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.from", is("must be greater than or equal to 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getOwnerBookingShouldReturnBadRequestWhenSizeZeroOrNegative() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .param("size", "0")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.size", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getUserBookingShouldReturnOkWhenValidRequestAndListSizeEqualZero() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserBookingShouldReturnOkWhenValidRequestAndListSizeIsPositive() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserBookingShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 0L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.userId", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getUserBookingShouldReturnBadRequestWhenFromNegative() throws Exception {
        mockMvc.perform(get("/bookings")
                        .param("from", "-1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.from", is("must be greater than or equal to 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getUserBookingShouldReturnBadRequestWhenSizeZeroOrNegative() throws Exception {
        mockMvc.perform(get("/bookings")
                        .param("size", "0")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.size", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getUserBookingShouldReturnBadRequestWhenNotValidState() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "alll")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Unknown state: alll")));
    }

    @Test
    void getByIdShouldReturnOkWhenValidRequest() throws Exception {
        long userId = 1L;
        mockMvc.perform(get("/bookings/{0}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getByIdShouldReturnBadRequestWhenBookingIdZeroOrNegative() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/bookings/{0}", 0L)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.bookingId", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getByIdShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        long userId = 0L;

        mockMvc.perform(get("/bookings/{0}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.userId", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }
}
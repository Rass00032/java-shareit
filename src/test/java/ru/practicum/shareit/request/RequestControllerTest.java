package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private RequestService requestService;
    @MockBean
    private RequestMapper requestMapper;

    private RequestDto requestDto;

    @BeforeEach
    public void set() {
        requestDto = RequestDto.builder()
                .description("I need to test")
                .build();
    }

    @Test
    void createRequestShouldReturnOkWhenValidRequest() throws Exception {
        long userId = 1L;

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createRequestShouldReturnBadRequestWhenRequestNull() throws Exception {
        long userId = 1L;
        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(null))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Неправильно сформированный запрос JSON")));
    }

    @Test
    void createRequestShouldReturnBadRequestWhenRequestNotValid() throws Exception {
        long userId = 1L;
        UserDto badRequest = UserDto.builder()
                .id(1L)
                .name("request name")
                .email("email")
                .build();

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(badRequest))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Ошибка валидации")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void createRequestShouldReturnBadRequestWhenUserIdNotValid() throws Exception {
        String userId = "1L";

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Failed to convert value of type")));
    }

    @Test
    void createRequestShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        long userId = 0L;

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Валидация не пройдена")))
                .andExpect(jsonPath("$.errors.userId", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void createRequestShouldReturnBadRequestWhenRequestDescriptionBlank() throws Exception {
        long userId = 1L;
        RequestDto badRequest = RequestDto.builder()
                .id(1L)
                .description(" ")
                .build();

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(badRequest))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Ошибка валидации")))
                .andExpect(jsonPath("$.errors.description", is("Описание не может быть пустым")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getAllShouldReturnOkWhenValidRequest() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequestShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 0L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Валидация не пройдена")))
                .andExpect(jsonPath("$.errors.userId", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getAllRequestShouldReturnBadRequestWhenFromNegative() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .param("from", "-1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Валидация не пройдена")))
                .andExpect(jsonPath("$.errors.from", is("must be greater than or equal to 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getAllRequestShouldReturnBadRequestWhenSizeZeroOrNegative() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .param("size", "0")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Валидация не пройдена")))
                .andExpect(jsonPath("$.errors.size", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getRequesterAllRequestShouldReturnOkWhenValidRequest() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getRequesterAllShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 0L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Валидация не пройдена")))
                .andExpect(jsonPath("$.errors.userId", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getRequesterAllShouldReturnBadRequestWhenFromNegative() throws Exception {
        mockMvc.perform(get("/requests")
                        .param("from", "-1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Валидация не пройдена")))
                .andExpect(jsonPath("$.errors.from", is("must be greater than or equal to 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getRequesterAllShouldReturnBadRequestWhenSizeZeroOrNegative() throws Exception {
        mockMvc.perform(get("/requests")
                        .param("size", "0")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Валидация не пройдена")))
                .andExpect(jsonPath("$.errors.size", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getByIdShouldReturnOkWhenValidRequest() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/requests/{0}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getByIdShouldReturnBadRequestWhenRequestIdZeroOrNegative() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/requests/{0}", 0L)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Валидация не пройдена")))
                .andExpect(jsonPath("$.errors.requestId", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void getByIdShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        long userId = 0L;

        mockMvc.perform(get("/requests/{0}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Валидация не пройдена")))
                .andExpect(jsonPath("$.errors.userId", is("must be greater than 0")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }
}
package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private final UserDto user = UserDto.builder()
            .id(1L)
            .name("John")
            .email("john@gam.com")
            .build();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserClient userClient;

    @Test
    void saveShouldReturnOkWhenValidRequest() throws Exception {

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void saveShouldReturnBadRequestWhenNotValidUserDto() throws Exception {
        HashMap<String, String> badDto = new HashMap<>();
        badDto.put("id", "1");
        badDto.put("firstName", "bad");

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(badDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.email", is("Email can not be null")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void saveShouldReturnBadRequestWhenEmailIsWrong() throws Exception {
        UserDto badUser = UserDto.builder()
                .name("John")
                .email("lets@lets").build();

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(badUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.email", is("Email is wrong")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void saveShouldReturnBadRequestWhenEmailNull() throws Exception {
        UserDto badUserDto = UserDto.builder()
                .name("John")
                .email(null).build();

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(badUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.email", is("Email can not be null")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void saveShouldReturnBadRequestWhenUserDtoIsNull() throws Exception {
        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(null))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Malformed JSON Request")));
    }

    @Test
    void editShouldReturnOkWhenValidRequest() throws Exception {
        // arrange
        long userId = 1L;

        UserDto updatedUser = UserDto.builder().build();
        updatedUser.setId(userId);
        updatedUser.setName("John Doe");
        updatedUser.setEmail("johndoe@example.com");

        // act
        mockMvc.perform(patch("/users/{0}", userId)
                        .content(mapper.writeValueAsString(updatedUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void editShouldReturnBadRequestWhenUserDtoIsNull() throws Exception {
        mockMvc.perform(patch("/users/{0}", 1L)
                        .content(mapper.writeValueAsString(null))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Malformed JSON Request")));
    }

    @Test
    void editShouldReturnBadRequestWhenUserDtoIdWrong() throws Exception {
        mockMvc.perform(patch("/users/{0}", "1L")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Failed to convert value of type")));
    }

    @Test
    void editShouldReturnBadRequestWhenUserDtoIdZeroOrNegative() throws Exception {
        mockMvc.perform(patch("/users/{0}", 0L)
                        .content(mapper.writeValueAsString(null))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Malformed JSON Request")));
    }

    @Test
    void getAllShouldReturnOk() throws Exception {
        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getByIdShouldReturnOkWhenValidRequest() throws Exception {
        long userId = 1L;
        user.setId(userId);

        mockMvc.perform(get("/users/{0}", userId)
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getByIdShouldReturnBadRequestWhenUserDtoIdWrong() throws Exception {
        mockMvc.perform(get("/users/{0}", "1L")
                        .content(mapper.writeValueAsString(null))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Failed to convert value of type")));
    }

    @Test
    void getByIdShouldReturnBadRequestWhenUserDtoIdZeroOrNegative() throws Exception {
        mockMvc.perform(get("/users/{0}", 0L)
                        .content(mapper.writeValueAsString(null))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.id", is("ID should be positive")))
                .andExpect((jsonPath("$.errors", aMapWithSize(1))));
    }

    @Test
    void deleteShouldReturnNoContentWhenValidRequest() throws Exception {
        long userId = 1L;

        mockMvc.perform(delete("/users/{0}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturnBadRequestWhenUserDtoIdIsWrong() throws Exception {
        mockMvc.perform(delete("/users/{0}", "0L")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteShouldReturnBadRequestWhenUserDtoIdZeroOrNegative() throws Exception {
        mockMvc.perform(delete("/users/{0}", 0L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
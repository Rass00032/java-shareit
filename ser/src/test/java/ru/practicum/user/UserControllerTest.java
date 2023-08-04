package ru.practicum.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void set() {
        user = User.builder()
                .name("john")
                .email("john.doe@mail.com").build();

        userDto = UserDto.builder()
                .id(1L)
                .name("Viktor")
                .email("viktor@gmail.com").build();
    }

    @Test
    void saveShouldReturnOkWhenValidRequest() throws Exception {
        User newUser = User.builder()
                .id(1L)
                .name(user.getName())
                .email(user.getEmail())
                .build();

        when(userService.add(any()))
                .thenReturn(newUser);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void editShouldReturnOkWhenValidRequest() throws Exception {
        // arrange
        long userId = 1L;

        User updatedUser = User.builder().build();
        updatedUser.setId(userId);
        updatedUser.setName("John Doe");
        updatedUser.setEmail("johndoe@example.com");


        when(userService.edit(anyLong(), any())).thenReturn(updatedUser);

        // act
        mockMvc.perform(patch("/users/{0}", userId)
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllShouldReturnOk() throws Exception {
        userDto.setId(1L);
        List<UserDto> users = new ArrayList<>();
        users.add(userDto);
        users.add(UserDto.builder()
                .id(2L).email("Sheldon.Cooper@big-bang.com")
                .name("Sheldon Lee Cooper")
                .build());

        when(userService.getAll())
                .thenReturn(users);

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
        when(userService.getById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/users/{0}", userId)
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
}
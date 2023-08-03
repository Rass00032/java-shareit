package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemClient itemClient;

    private ItemDto itemDto;

    @BeforeEach
    public void set() {
        itemDto = ItemDto.builder()
                .name("Drel")
                .description("Дрель ударная")
                .available(true)
                .build();
    }

    @Test
    void saveShouldReturnOkWhenValidRequest() throws Exception {
        long userId = 1L;

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void saveShouldReturnBadRequestWhenItemNull() throws Exception {
        long userId = 1L;
        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(null))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnBadRequestWhenItemNotValid() throws Exception {
        long userId = 1L;
        UserDto badItem = UserDto.builder()
                .id(1L)
                .name(itemDto.getName())
                .email(itemDto.getDescription())
                .build();

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(badItem))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnBadRequestWhenUserIdNotValid() throws Exception {
        String userId = "1L";

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        long userId = 0L;

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnBadRequestWhenItemNameBlank() throws Exception {
        long userId = 1L;
        ItemDto badItem = ItemDto.builder()
                .id(1L)
                .name(" ")
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(badItem))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnBadRequestWhenItemDescriptionBlank() throws Exception {
        long userId = 1L;
        ItemDto badItem = ItemDto.builder()
                .id(1L)
                .name(itemDto.getName())
                .description(" ")
                .available(itemDto.getAvailable())
                .build();

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(badItem))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShouldReturnBadRequestWhenItemAvailableNull() throws Exception {
        long userId = 1L;
        ItemDto badItem = ItemDto.builder()
                .id(1L)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(null)
                .build();

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(badItem))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editShouldReturnOkWhenValidRequest() throws Exception {
        long userId = 1L;
        long itemId = 1L;

        mockMvc.perform(patch("/items/{0}", itemId)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void editShouldReturnBadRequestWhenItemNull() throws Exception {
        long userId = 1L;
        mockMvc.perform(patch("/items/{0}", 1L)
                        .content(mapper.writeValueAsString(null))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editShouldReturnBadRequestWhenItemNotValid() throws Exception {
        long userId = 1L;
        UserDto badItem = UserDto.builder()
                .email(itemDto.getDescription())
                .build();

        mockMvc.perform(patch("/items/{0}", badItem.getId())
                        .content(mapper.writeValueAsString(badItem))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editShouldReturnBadRequestWhenUserIdNotValid() throws Exception {
        String userId = "1L";
        UserDto badItem = UserDto.builder()
                .id(1L)
                .name(itemDto.getName())
                .email(itemDto.getDescription())
                .build();

        mockMvc.perform(patch("/items/{0}", badItem.getId())
                        .content(mapper.writeValueAsString(badItem))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        long userId = 0L;

        mockMvc.perform(patch("/items/{0}", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editShouldReturnBadRequestWhenNoUserId() throws Exception {
        ItemDto badItem = ItemDto.builder()
                .id(1L)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(null)
                .build();

        mockMvc.perform(patch("/items/{0}", badItem.getId())
                        .content(mapper.writeValueAsString(badItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllShouldReturnOkWhenValidRequest() throws Exception {
        mockMvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        mockMvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", 0L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllShouldReturnBadRequestWhenFromNegative() throws Exception {
        mockMvc.perform(get("/items")
                        .param("from", "-1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllShouldReturnBadRequestWhenSizeZeroOrNegative() throws Exception {
        mockMvc.perform(get("/items")
                        .param("size", "0")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByIdShouldReturnOkWhenValidRequest() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/items/{0}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getByIdShouldReturnBadRequestWhenUserIdZeroOrNegative() throws Exception {
        long userId = 0L;

        mockMvc.perform(get("/items/{0}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchItemsShouldReturnOkWhenValidRequest() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void searchItemsShouldReturnOkWhenTextIsBlank() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", " ")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void searchItemsShouldReturnBadRequestWhenFromIsNegative() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "rrr")
                        .param("from", "-1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchItemsShouldReturnBadRequestWhenSizeIsZeroOrNegative() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "rrr")
                        .param("size", "0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveCommentShouldReturnOkWhenValidRequest() throws Exception {
        long userId = 1L;
        CommentDto comment = CommentDto.builder()
                .id(1L)
                .text("super puper")
                .build();

        mockMvc.perform(post("/items/{0}/comment", 1L)
                        .content(mapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void saveCommentShouldReturnBadRequestWhenCommentTextIsBlank() throws Exception {
        long userId = 1L;
        CommentDto comment = CommentDto.builder()
                .text(" ")
                .build();

        mockMvc.perform(post("/items/{0}/comment", 1L)
                        .content(mapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveCommentShouldReturnBadRequestWhenUserIdIsZeroOrNegative() throws Exception {
        long userId = 0L;
        CommentDto comment = CommentDto.builder()
                .text(" ")
                .build();

        mockMvc.perform(post("/items/{0}/comment", 1L)
                        .content(mapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveCommentShouldReturnBadRequestWhenItemIdIsZeroOrNegative() throws Exception {
        long userId = 1L;
        CommentDto comment = CommentDto.builder()
                .text(" ")
                .build();

        mockMvc.perform(post("/items/{0}/comment", 0L)
                        .content(mapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
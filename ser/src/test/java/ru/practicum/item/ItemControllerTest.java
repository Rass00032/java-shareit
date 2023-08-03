package ru.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemMapper itemMapper;

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
    void getAllShouldReturnOkWhenValidRequest() throws Exception {
        mockMvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
}
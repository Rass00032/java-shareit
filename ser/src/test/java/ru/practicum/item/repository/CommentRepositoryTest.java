package ru.practicum.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.TestDataInitializer;
import ru.practicum.item.model.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(TestDataInitializer.class)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TestDataInitializer testData;

    @Test
    void findByItemId() {
        List<Comment> result = commentRepository.findByItemId(3L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getText()).contains("fine");
    }
}
package ru.practicum.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.TestDataInitializer;
import ru.practicum.request.model.Request;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestDataInitializer.class)
class RequestRepositoryTest {

    private final Pageable pageable = PageRequest.of(0, 10);
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private TestDataInitializer testData;

    @Test
    void testFindAllByRequesterIdNot() {
        List<Request> result = requestRepository.findAllByRequesterIdNot(testData.getDen().getId(), pageable).toList();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).contains("I need siphon. Vik");
    }

    @Test
    void testFindAllByRequester() {
        List<Request> result = requestRepository.findAllByRequester(testData.getVik(), pageable).toList();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).contains("I need siphon. Vik");
    }
}
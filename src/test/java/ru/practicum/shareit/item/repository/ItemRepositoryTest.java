package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.TestDataInitializer;
import ru.practicum.shareit.item.repository.model.Item;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(TestDataInitializer.class)
class ItemRepositoryTest {

    private final Pageable pageable = PageRequest.of(0, 10);

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private TestDataInitializer testData;

    @Test
    void findAllByOwnerOrderByIdAsc() {
        List<Item> result = itemRepository.findAllByOwnerOrderByIdAsc(testData.getVik(), pageable).toList();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).contains("дрель");
        assertThat(result.get(1).getName()).contains("отвертка");
    }


    @Test
    void findLikingByNameOrDescription() {
        List<Item> result = itemRepository.findLikingByNameOrDescription("Аккумулятор", pageable).toList();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).contains("дрель");
        assertThat(result.get(1).getName()).contains("отвертка");
    }

    @Test
    void findLikingByNameOrDescriptionShouldReturnOneWhenAvailableFalse() {
        Item item = testData.getItem1().toBuilder().available(false).build();
        itemRepository.save(item);
        List<Item> result = itemRepository.findLikingByNameOrDescription("Аккумулятор", pageable).toList();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).contains("отвертка");
    }

    @Test
    void findAllByRequestOrderByIdAsc() {
        List<Item> result = itemRepository.findAllByRequestOrderByIdAsc(3L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).contains("siphon");
    }
}
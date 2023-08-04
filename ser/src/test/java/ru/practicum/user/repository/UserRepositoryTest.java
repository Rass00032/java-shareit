package ru.practicum.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.TestDataInitializer;
import ru.practicum.user.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(TestDataInitializer.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestDataInitializer testData;

    @Test
    void findByEmailShouldReturnUser() {
        Optional<User> result = userRepository.findByEmail(testData.getVik().getEmail());

        assertThat(result).isNotEmpty();
        assertThat(result.get().getName()).isEqualTo("vik");
    }

    @Test
    void findByEmailShouldReturnEmpty() {
        Optional<User> result = userRepository.findByEmail("vik@gmail.co");

        assertThat(result).isEmpty();
    }
}
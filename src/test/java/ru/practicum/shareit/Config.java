package ru.practicum.shareit;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class Config {
    @Bean
    public TestDataInitializer testDataInitializer() {
        return new TestDataInitializer();
    }
}

package com.yashmerino.url_shortener.repository;

import com.yashmerino.url_shortener.model.UrlMapping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlMappingRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Test
    void existsByShortCode_ShouldReturnTrue_WhenCodeExists() {
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl("https://google.com")
                .shortCode("googl1")
                .createdAt(LocalDateTime.now())
                .isArchived(false)
                .build();
        urlMappingRepository.save(mapping);

        boolean exists = urlMappingRepository.existsByShortCode("googl1");
        assertThat(exists).isTrue();
    }

    @Test
    void existsByShortCode_ShouldReturnFalse_WhenCodeExists() {
        boolean exists = urlMappingRepository.existsByShortCode("nonexistent");
        assertThat(exists).isFalse();
    }
}
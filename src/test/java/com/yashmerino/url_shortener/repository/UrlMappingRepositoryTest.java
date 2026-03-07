package com.yashmerino.url_shortener.repository;

import com.yashmerino.url_shortener.config.TestCacheConfig;
import com.yashmerino.url_shortener.model.UrlMapping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestCacheConfig.class)
class UrlMappingRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Test
    void existsByShortCode_ShouldReturnTrue_WhenCodeExists() {
        this.createUrlMapping("https://google.com", "googl1");

        boolean exists = urlMappingRepository.existsByShortCode("googl1");
        assertThat(exists).isTrue();
    }

    @Test
    void existsByShortCode_ShouldReturnFalse_WhenCodeExists() {
        boolean exists = urlMappingRepository.existsByShortCode("nonexistent");
        assertThat(exists).isFalse();
    }

    @Test
    void findByShortCode_ShouldReturnObject_WhenItExists() {
        this.createUrlMapping("https://google.com", "googl1");

        final UrlMapping urlMapping = urlMappingRepository.findByShortCode("googl1");
        assertThat(urlMapping).isNotNull();
        assertThat(urlMapping.getShortCode()).isEqualTo("googl1");
        assertThat(urlMapping.getOriginalUrl()).isEqualTo("https://google.com");
    }

    @Test
    void findByShortCode_ShouldReturnNull_WhenItDoesNotExist() {
        final UrlMapping urlMappingOptional = urlMappingRepository.findByShortCode("googl1");

        assertThat(urlMappingOptional).isNull();
    }

    private void createUrlMapping(final String originalURL, final String shortCode) {
        UrlMapping mapping = UrlMapping.builder()
                .originalUrl(originalURL)
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .isArchived(false)
                .build();

        urlMappingRepository.save(mapping);
    }
}
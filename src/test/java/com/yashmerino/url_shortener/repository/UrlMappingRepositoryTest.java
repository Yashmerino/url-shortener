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

    @Test
    void findByCreatedAtAfterAndIsArchivedFalse_ShouldReturnUrlsFromLastHour() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);

        UrlMapping url1 = UrlMapping.builder()
                .originalUrl("https://google.com")
                .shortCode("googl1")
                .createdAt(thirtyMinutesAgo)
                .isArchived(false)
                .build();

        UrlMapping url2 = UrlMapping.builder()
                .originalUrl("https://github.com")
                .shortCode("gith2")
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .isArchived(false)
                .build();

        UrlMapping url3 = UrlMapping.builder()
                .originalUrl("https://archived.com")
                .shortCode("arch3")
                .createdAt(thirtyMinutesAgo)
                .isArchived(true)
                .build();

        UrlMapping url4 = UrlMapping.builder()
                .originalUrl("https://old.com")
                .shortCode("old4")
                .createdAt(twoHoursAgo)
                .isArchived(false)
                .build();

        urlMappingRepository.save(url1);
        urlMappingRepository.save(url2);
        urlMappingRepository.save(url3);
        urlMappingRepository.save(url4);

        var result = urlMappingRepository.findByCreatedAtAfterAndIsArchivedFalseOrderByCreatedAtDesc(oneHourAgo);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getShortCode()).isEqualTo("gith2");
        assertThat(result.get(1).getShortCode()).isEqualTo("googl1");
    }

    @Test
    void findByCreatedAtAfterAndIsArchivedFalse_ShouldReturnEmpty_WhenNoUrlsInTimeRange() {
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);

        UrlMapping url = UrlMapping.builder()
                .originalUrl("https://old.com")
                .shortCode("old1")
                .createdAt(twoHoursAgo)
                .isArchived(false)
                .build();

        urlMappingRepository.save(url);

        var result = urlMappingRepository.findByCreatedAtAfterAndIsArchivedFalseOrderByCreatedAtDesc(LocalDateTime.now().minusHours(1));

        assertThat(result).isEmpty();
    }

    @Test
    void findByCreatedAtAfterAndIsArchivedFalse_ShouldOrderByCreatedAtDescending() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        LocalDateTime now = LocalDateTime.now();

        UrlMapping url1 = UrlMapping.builder()
                .originalUrl("https://first.com")
                .shortCode("first1")
                .createdAt(oneHourAgo.plusMinutes(1))
                .isArchived(false)
                .build();

        UrlMapping url2 = UrlMapping.builder()
                .originalUrl("https://second.com")
                .shortCode("second2")
                .createdAt(now)
                .isArchived(false)
                .build();

        UrlMapping url3 = UrlMapping.builder()
                .originalUrl("https://third.com")
                .shortCode("third3")
                .createdAt(oneHourAgo.plusMinutes(30))
                .isArchived(false)
                .build();

        urlMappingRepository.save(url1);
        urlMappingRepository.save(url2);
        urlMappingRepository.save(url3);

        var result = urlMappingRepository.findByCreatedAtAfterAndIsArchivedFalseOrderByCreatedAtDesc(oneHourAgo);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getShortCode()).isEqualTo("second2");
        assertThat(result.get(1).getShortCode()).isEqualTo("third3");
        assertThat(result.get(2).getShortCode()).isEqualTo("first1");
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
package com.yashmerino.url_shortener.service;

import com.yashmerino.url_shortener.model.UrlMapping;
import com.yashmerino.url_shortener.model.dto.ShortUrlDTO;
import com.yashmerino.url_shortener.repository.UrlMappingRepository;
import com.yashmerino.url_shortener.service.impl.UrlMappingServiceImpl;
import com.yashmerino.url_shortener.util.HashUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlMappingServiceImplTest {

    @Mock
    private UrlMappingRepository urlMappingRepository;

    @InjectMocks
    private UrlMappingServiceImpl urlMappingService;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void shorten_ShouldSaveUrlAndGenerateBase62ShortCode() {
        final String originalURL = "https://google.com";
        
        UrlMapping savedMapping = UrlMapping.builder()
                .id(1L)
                .originalUrl(originalURL)
                .createdAt(LocalDateTime.now())
                .isArchived(false)
                .build();
        
        when(urlMappingRepository.save(any(UrlMapping.class))).thenReturn(savedMapping);
        
        ShortUrlDTO result = urlMappingService.shorten(originalURL);
        
        assertThat(result).isNotNull();
        assertThat(result.getOriginalUrl()).isEqualTo(originalURL);
        assertThat(result.getShortCode()).isEqualTo("1");
        assertThat(result.getShortUrl()).endsWith("/1");
        
        verify(urlMappingRepository, times(2)).save(any(UrlMapping.class));
    }

    @Test
    void shorten_ShouldCreateShortUrlWithCorrectBase62Format() {
        final String originalURL = "https://example.com";
        
        UrlMapping savedMapping = UrlMapping.builder()
                .id(42L)
                .originalUrl(originalURL)
                .createdAt(LocalDateTime.now())
                .isArchived(false)
                .build();
        
        when(urlMappingRepository.save(any(UrlMapping.class))).thenReturn(savedMapping);
        
        ShortUrlDTO result = urlMappingService.shorten(originalURL);
        
        String expectedShortCode = HashUtils.generateShortCode(42L);
        assertThat(result.getShortCode()).isEqualTo(expectedShortCode);
        assertThat(result.getShortUrl()).endsWith("/" + expectedShortCode);
    }

    @Test
    void shorten_ShouldUseBase62EncodingForIds() {
        String code1 = HashUtils.generateShortCode(1L);
        String code2 = HashUtils.generateShortCode(2L);
        String code1Again = HashUtils.generateShortCode(1L);
        
        assertThat(code1).isNotEqualTo(code2);
        assertThat(code1).isEqualTo(code1Again);
        assertThat(code1).matches("[0-9A-Za-z]+");
    }

    @Test
    void redirect_ShouldReturnCorrectMapping() {
        final String originalURL = "https://example.com";

        UrlMapping savedMapping = UrlMapping.builder()
                .id(42L)
                .originalUrl(originalURL)
                .shortCode("example")
                .createdAt(LocalDateTime.now())
                .isArchived(false)
                .build();

        when(urlMappingRepository.findByShortCode("example")).thenReturn(savedMapping);

        UrlMapping urlMapping = urlMappingService.redirect("example");

        assertThat(urlMapping.getShortCode()).isEqualTo("example");
        assertThat(urlMapping.getOriginalUrl()).endsWith(originalURL);
    }

    @Test
    void redirect_ShouldReturnNull_WhenDoesNotExist() {
        when(urlMappingRepository.findByShortCode("error")).thenReturn(null);

        UrlMapping urlMapping = urlMappingService.redirect("error");
        assertThat(urlMapping).isNull();
    }
}
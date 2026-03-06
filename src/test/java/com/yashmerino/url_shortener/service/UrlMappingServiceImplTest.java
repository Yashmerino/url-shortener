package com.yashmerino.url_shortener.service;

import com.yashmerino.url_shortener.model.UrlMapping;
import com.yashmerino.url_shortener.model.dto.ShortUrlDTO;
import com.yashmerino.url_shortener.model.dto.UrlMappingPostDTO;
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
        // Simulăm un request HTTP pentru ca ServletUriComponentsBuilder să nu crape
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void shorten_ShouldSaveUrl_WhenShortCodeDoesNotExist() {
        UrlMappingPostDTO inputDto = new UrlMappingPostDTO("https://google.com");
        String expectedCode = HashUtils.generateMD5(inputDto.getOriginalUrl()).substring(0, 6);
        
        when(urlMappingRepository.existsByShortCode(expectedCode)).thenReturn(false);

        ShortUrlDTO result = urlMappingService.shorten(inputDto);

        assertThat(result.getShortCode()).isEqualTo(expectedCode);
        verify(urlMappingRepository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    void shorten_ShouldNotSaveUrl_WhenShortCodeAlreadyExists() {
        UrlMappingPostDTO inputDto = new UrlMappingPostDTO("https://google.com");
        String expectedCode = HashUtils.generateMD5(inputDto.getOriginalUrl()).substring(0, 6);
        
        when(urlMappingRepository.existsByShortCode(expectedCode)).thenReturn(true);

        urlMappingService.shorten(inputDto);

        verify(urlMappingRepository, never()).save(any(UrlMapping.class));
    }
}
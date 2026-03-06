package com.yashmerino.url_shortener.service.impl;

import com.yashmerino.url_shortener.model.UrlMapping;
import com.yashmerino.url_shortener.model.dto.ShortUrlDTO;
import com.yashmerino.url_shortener.model.dto.UrlMappingPostDTO;
import com.yashmerino.url_shortener.repository.UrlMappingRepository;
import com.yashmerino.url_shortener.service.UrlMappingService;
import com.yashmerino.url_shortener.util.HashUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

/**
 * Shortener service base implementation.
 */
@Service
@RequiredArgsConstructor
public class UrlMappingServiceImpl implements UrlMappingService {

    /**
     * URL Mapping repository.
     */
    private final UrlMappingRepository urlMappingRepository;

    /**
     * Shortens the original URL
     *
     * @param urlMappingPostDTO is the original URL object.
     *
     * @return the Short URL DTO.
     */
    @Override
    @Transactional
    public ShortUrlDTO shorten(UrlMappingPostDTO urlMappingPostDTO) {
        final String originalURL = urlMappingPostDTO.getOriginalUrl();
        final String shortCode = HashUtils.generateMD5(originalURL).substring(0, 6);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString();
        String shortUrl = baseUrl + "/" + shortCode;

        if(!urlMappingRepository.existsByShortCode(shortCode)) {
             UrlMapping urlMapping = UrlMapping.builder()
                    .originalUrl(urlMappingPostDTO.getOriginalUrl())
                    .shortCode(shortCode)
                    .createdAt(LocalDateTime.now())
                    .build();

            urlMappingRepository.save(urlMapping);
        }

        return ShortUrlDTO.builder()
                .originalUrl(originalURL)
                .shortCode(shortCode)
                .shortUrl(shortUrl)
                .build();
    }

    /**
     * Redirects the user to original URL.
     *
     * @param shortCode is the original URL's short code.
     * @return the URL Mapping entity.
     */
    @Override
    public UrlMapping redirect(String shortCode) {
        return null;
    }
}

package com.yashmerino.url_shortener.service.impl;

import com.yashmerino.url_shortener.model.UrlMapping;
import com.yashmerino.url_shortener.model.dto.ShortUrlDTO;
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
     * @param originalURL is the original URL.
     *
     * @return the Short URL DTO.
     */
    @Override
    @Transactional
    public ShortUrlDTO shorten(final String originalURL) {
        // Create and save the URL mapping first to get the ID
        UrlMapping urlMapping = UrlMapping.builder()
                .originalUrl(originalURL)
                .createdAt(LocalDateTime.now())
                .isArchived(false)
                .build();

        UrlMapping savedMapping = urlMappingRepository.save(urlMapping);
        
        // Generate short code based on the saved ID using Base62
        String shortCode = HashUtils.generateShortCode(savedMapping.getId());
        
        // Update the short code in the database
        savedMapping.setShortCode(shortCode);
        urlMappingRepository.save(savedMapping);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString();
        String shortUrl = baseUrl + "/" + shortCode;

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
        return this.urlMappingRepository.findByShortCode(shortCode);
    }
}

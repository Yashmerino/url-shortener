package com.yashmerino.url_shortener.service;

import com.yashmerino.url_shortener.model.UrlMapping;
import com.yashmerino.url_shortener.model.dto.ShortUrlDTO;
import com.yashmerino.url_shortener.model.dto.UrlMappingPostDTO;

/**
 * Shortener service interface.
 */
public interface UrlMappingService {
    /**
     * Shortens the original URL
     *
     * @param urlMappingPostDTO is the original URL object.
     *
     * @return the Short URL DTO.
     */
    ShortUrlDTO shorten(final UrlMappingPostDTO urlMappingPostDTO);

    /**
     * Redirects the user to original URL.
     *
     * @param shortCode is the original URL's short code.
     *
     * @return the URL Mapping entity.
     */
    UrlMapping redirect(final String shortCode);
}

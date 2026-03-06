package com.yashmerino.url_shortener.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * JPA Entity representing short URL DTO.
 */
@Builder
@AllArgsConstructor
@Data
public class ShortUrlDTO {
    /**
     * Short Code
     */
    private String shortCode;

    /**
     * Short URL
     */
    private String shortUrl;

    /**
     * Original URL.
     */
    private String originalUrl;
}

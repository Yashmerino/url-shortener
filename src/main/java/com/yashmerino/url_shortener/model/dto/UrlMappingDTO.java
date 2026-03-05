package com.yashmerino.url_shortener.model.dto;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * JPA Entity representing URL Mapping object.
 */
public class UrlMappingDTO {
    /**
     * Entity id.
     */
    private Long id;

    /**
     * Original URL.
     */
    private String originalUrl;

    /**
     * When was entity created.
     */
    private LocalDateTime createdAt;
}

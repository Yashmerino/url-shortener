package com.yashmerino.url_shortener.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * JPA Entity representing URL Mapping object.
 */
@Entity(name = "url_mapping")
@Table(name = "url_mapping")
public class UrlMapping {
    /**
     * Entity id.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
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

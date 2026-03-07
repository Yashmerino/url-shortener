package com.yashmerino.url_shortener.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * JPA Entity representing URL Mapping object.
 */
@Entity(name = "url_mapping")
@Table(name = "url_mapping")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UrlMapping {
    /**
     * Entity id.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * Original URL.
     */
    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    /**
     * Short code.
     */
    @Column(name = "short_code")
    private String shortCode;

    /**
     * When was entity created.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * If the mapping was archived or not.
     */
    @Column(name = "is_archived", nullable = false)
    private boolean isArchived;
}

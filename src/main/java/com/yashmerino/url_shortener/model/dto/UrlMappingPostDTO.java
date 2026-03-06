package com.yashmerino.url_shortener.model.dto;

import lombok.Data;

/**
 * JPA Entity representing URL Mapping post object.
 */
@Data
public class UrlMappingPostDTO {
    /**
     * Original URL.
     */
    private String originalUrl;
}

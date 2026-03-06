package com.yashmerino.url_shortener.controller;

import com.yashmerino.url_shortener.model.dto.ShortUrlDTO;
import com.yashmerino.url_shortener.model.dto.UrlMappingPostDTO;
import com.yashmerino.url_shortener.service.UrlMappingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for URL shortener API.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UrlMappingRestController {

    /**
     * URL Mapping service object.
     */
    private final UrlMappingService urlMappingService;

    @Operation(summary = "Shortens an URL", description = "Converts a long URL into a short URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "URL created successfully"),
            @ApiResponse(responseCode = "400", description = "URL is invalid.")
    })
    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlDTO> shorten(@Validated @RequestBody UrlMappingPostDTO urlMappingPostDTO) {
        final ShortUrlDTO shortUrlDTO = urlMappingService.shorten(urlMappingPostDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrlDTO);
    }
}

package com.yashmerino.url_shortener.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Custom Open API Bean configuration.
     *
     * @return the bean.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("URL Shortener")
                        .description("This document provides API details for the URL Shortener application."));
    }
}
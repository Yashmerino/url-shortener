package com.yashmerino.url_shortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Returns the security filter chain Bean.
     *
     * @param http is the security object.
     *
     * @return the bean.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**", "/api/v1/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
            .logout(LogoutConfigurer::permitAll);

        return http.build();
    }
}
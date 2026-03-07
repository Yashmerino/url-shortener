package com.yashmerino.url_shortener.controller;

import com.yashmerino.url_shortener.model.UrlMapping;
import com.yashmerino.url_shortener.model.dto.ShortUrlDTO;
import com.yashmerino.url_shortener.service.UrlMappingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ViewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlMappingService urlMappingService;

    @Test
    void index_ShouldReturnIndexTemplate() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void shortenUrl_ShouldReturnResultWhenSuccessful() throws Exception {
        ShortUrlDTO responseDto = ShortUrlDTO
                .builder()
                .originalUrl("https://google.com")
                .shortCode("abc123")
                .shortUrl("http://localhost/abc123")
                .build();

        when(urlMappingService.shorten(any(String.class))).thenReturn(responseDto);

        mockMvc.perform(post("/")
                        .param("originalUrl", "https://google.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(flash().attributeExists("shortUrl"))
                .andExpect(flash().attribute("shortUrl", "http://localhost/abc123"))
                .andExpect(flash().attribute("shortCode", "abc123"))
                .andExpect(flash().attribute("originalUrl", "https://google.com"));
    }

    @Test
    void shortenUrl_ShouldReturnErrorWhenServiceThrowsException() throws Exception {
        when(urlMappingService.shorten(any(String.class)))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/")
                        .param("originalUrl", "https://google.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void redirect_ShouldRedirect_WhenSuccessful() throws Exception {
        final UrlMapping urlMapping = UrlMapping.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .isArchived(false)
                .originalUrl("https://google.com")
                .shortCode("googl1")
                .build();

        when(urlMappingService.redirect(any(String.class)))
                .thenReturn(urlMapping);

        mockMvc.perform(get("/googl1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:https://google.com"));
    }

    @Test
    void redirect_ShouldRedirectToError_WhenDoesNotExist() throws Exception {
        when(urlMappingService.shorten(any(String.class))).thenReturn(null);

        mockMvc.perform(get("/googl1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/notfound"));
    }
}

package com.yashmerino.url_shortener.controller;

import com.yashmerino.url_shortener.model.dto.ShortUrlDTO;
import com.yashmerino.url_shortener.service.UrlMappingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("shortUrl"))
                .andExpect(model().attribute("shortUrl", "http://localhost/abc123"))
                .andExpect(model().attribute("shortCode", "abc123"))
                .andExpect(model().attribute("originalUrl", "https://google.com"));
    }

    @Test
    void shortenUrl_ShouldReturnErrorWhenServiceThrowsException() throws Exception {
        when(urlMappingService.shorten(any(String.class)))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/")
                        .param("originalUrl", "https://google.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("originalUrl", "https://google.com"));
    }
}

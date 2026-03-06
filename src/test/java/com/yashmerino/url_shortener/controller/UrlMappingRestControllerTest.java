package com.yashmerino.url_shortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yashmerino.url_shortener.model.dto.ShortUrlDTO;
import com.yashmerino.url_shortener.model.dto.UrlMappingPostDTO;
import com.yashmerino.url_shortener.service.UrlMappingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UrlMappingRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class UrlMappingRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlMappingService urlMappingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shorten_ShouldReturn201_WhenRequestIsValid() throws Exception {
        UrlMappingPostDTO requestDto = new UrlMappingPostDTO("https://google.com");
        ShortUrlDTO responseDto = ShortUrlDTO
                .builder()
                .originalUrl("https://google.com")
                .shortCode("abc123")
                .shortUrl("http://localhost/abc123")
                .build();

        when(urlMappingService.shorten(any(UrlMappingPostDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").value("abc123"))
                .andExpect(jsonPath("$.originalUrl").value("https://google.com"));
    }

    @Test
    void shorten_ShouldReturn400_WhenBodyIsMissing() throws Exception {
        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
}
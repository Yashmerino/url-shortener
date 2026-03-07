package com.yashmerino.url_shortener.controller;

import com.yashmerino.url_shortener.service.UrlMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling Thymeleaf view requests.
 */
@Controller
@RequiredArgsConstructor
public class ViewController {

    /**
     * URL Mapping service object.
     */
    private final UrlMappingService urlMappingService;

    /**
     * Display the URL shortener form page.
     *
     * @param model the model to add attributes to.
     * @return the template name for the shortener form.
     */
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    /**
     * Handle the shortening of a URL from the form and display the result.
     *
     * @param originalUrl the original URL to shorten.
     * @param model the model to add attributes to.
     * @return the template name for the shortener form with result.
     */
    @PostMapping("/")
    public String shortenUrl(@RequestParam String originalUrl, Model model) {
        try {
            final var shortUrlDTO = urlMappingService.shorten(originalUrl);
            model.addAttribute("shortUrl", shortUrlDTO.getShortUrl());
            model.addAttribute("shortCode", shortUrlDTO.getShortCode());
            model.addAttribute("originalUrl", originalUrl);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to shorten URL: " + e.getMessage());
            model.addAttribute("originalUrl", originalUrl);
        }
        return "index";
    }

    /**
     * Redirect to the original URL using the short code.
     *
     * @param shortCode the short code.
     * @return redirect to original URL or not found page.
     */
    @GetMapping("/redirect/{shortCode}")
    public String redirect(@PathVariable String shortCode) {
        return "error/notfound";
    }
}

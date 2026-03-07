package com.yashmerino.url_shortener.controller;

import com.yashmerino.url_shortener.model.UrlMapping;
import com.yashmerino.url_shortener.service.UrlMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

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
     * Display the recently created URLs page (from the last hour).
     *
     * @param model the model to add attributes to.
     * @return the template name for the recent URLs page.
     */
    @GetMapping("/recent")
    public String recent(Model model) {
        List<UrlMapping> recentUrls = urlMappingService.getRecentUrls();
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString();
        model.addAttribute("recentUrls", recentUrls);
        model.addAttribute("baseUrl", baseUrl);
        return "recent";
    }

    /**
     * Handle the shortening of a URL from the form and display the result.
     *
     * @param originalUrl the original URL to shorten.
     * @param redirectAttributes the redirect attributes.
     *
     * @return the template name for the shortener form with result.
     */
    @PostMapping("/")
    public String shortenUrl(@RequestParam String originalUrl, RedirectAttributes redirectAttributes) {
        try {
            final var shortUrlDTO = urlMappingService.shorten(originalUrl);
            redirectAttributes.addFlashAttribute("shortUrl", shortUrlDTO.getShortUrl());
            redirectAttributes.addFlashAttribute("shortCode", shortUrlDTO.getShortCode());
            redirectAttributes.addFlashAttribute("originalUrl", originalUrl);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed: " + e.getMessage());
        }

        return "redirect:/";
    }

    /**
     * Redirect to the original URL using the short code.
     *
     * @param shortCode the short code.
     * @return redirect to original URL or not found page.
     */
    @GetMapping("/{shortCode}")
    public String redirect(@PathVariable String shortCode) {
        UrlMapping mapping = urlMappingService.redirect(shortCode);
        if (mapping != null) {
            return "redirect:" + mapping.getOriginalUrl();
        }
        return "error/notfound";
    }
}

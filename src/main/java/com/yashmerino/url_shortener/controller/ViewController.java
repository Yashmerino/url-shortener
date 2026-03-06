package com.yashmerino.url_shortener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller for handling Thymeleaf view requests.
 */
@Controller
@RequiredArgsConstructor
public class ViewController {

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

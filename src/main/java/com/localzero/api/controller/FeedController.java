package com.localzero.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeedController {

    @GetMapping("/feed")
    public String feed() {
        return "feed"; //TODO: skapa feed.html i templates-mappen
    }
}
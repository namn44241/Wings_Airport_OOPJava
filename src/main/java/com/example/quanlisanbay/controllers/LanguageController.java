package com.example.quanlisanbay.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LanguageController {
    
    private static final Logger logger = LoggerFactory.getLogger(LanguageController.class);
    
    @GetMapping("/san_bay.html")
    public String getVietnamesePage() {
        logger.info("Switching to Vietnamese page");
        return "san_bay";
    }

    @GetMapping("/san_bay_en.html")
    public String getEnglishPage() {
        logger.info("Switching to English page");
        return "san_bay_en";
    }
}
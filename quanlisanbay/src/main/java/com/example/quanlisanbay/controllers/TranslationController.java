package com.example.quanlisanbay.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quanlisanbay.config.JsonMessageSource;

@RestController
public class TranslationController {

    @Autowired
    private JsonMessageSource jsonMessageSource;

    @GetMapping("/translate")
    public ResponseEntity<?> translate(
            @RequestParam(name = "key") String key,
            @RequestParam(name = "lang", defaultValue = "vi") String lang) {
        try {
            if ("all".equals(key)) {
                Map<String, String> messages = jsonMessageSource.getMessages(lang);
                return ResponseEntity.ok(messages);
            } else {
                String message = jsonMessageSource.getMessage(key, lang);
                return ResponseEntity.ok(message);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }

}
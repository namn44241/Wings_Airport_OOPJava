package com.example.quanlisanbay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.quanlisanbay.config.EmailService;
import com.example.quanlisanbay.model.EmailRequest;
import com.example.quanlisanbay.model.EmailResponse;

@RestController
@CrossOrigin(origins = "*")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request) {
        try {
            emailService.sendEmail(request.getEmail(), request.getLanguage(), request.getFullName());
            return ResponseEntity.ok().body(new EmailResponse(true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new EmailResponse(false, e.getMessage()));
        }
    }
}
package com.example.quanlisanbay.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.quanlisanbay.config.LoginRequired;

@Controller
public class AdminController {

    @GetMapping("/admin")
    @LoginRequired
    public String sanBay() {
        return "admin";
    }
}
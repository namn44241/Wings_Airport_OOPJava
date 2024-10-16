package com.example.quanlisanbay.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @GetMapping("/")
    public String checkLogin(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "redirect:/admin";
        } else {
            return "redirect:/san_bay";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
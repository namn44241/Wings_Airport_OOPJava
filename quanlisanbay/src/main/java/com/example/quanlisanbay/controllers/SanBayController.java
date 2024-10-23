package com.example.quanlisanbay.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanBayController {

    @GetMapping("/san_bay")
    public String sanBay() {
        return "san_bay";
    }
    // public String sanBayPage() {
    //     return "san_bay"; // Trả về tên file san_bay.html
    // }
}
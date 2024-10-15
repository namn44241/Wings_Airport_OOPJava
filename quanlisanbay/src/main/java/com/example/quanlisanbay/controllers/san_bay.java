package com.example.quanlisanbay.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class san_bay {

    @GetMapping("/")
    public ModelAndView home() {
        // Chuyển hướng đến trang san_bay.html
        return new ModelAndView("san_bay"); 
    }
}

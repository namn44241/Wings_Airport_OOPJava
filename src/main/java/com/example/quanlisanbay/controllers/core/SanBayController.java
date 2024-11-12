package com.example.quanlisanbay.controllers.core;

import com.example.quanlisanbay.service.SanBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
public class SanBayController {

    @Autowired
    private SanBayService sanBayService;

    @GetMapping("/san_bay")
    public String sanBay() {
        return "san_bay";
    }

    @GetMapping("/api/flights")
    @ResponseBody
    public Map<String, Object> getFlights() {
        return sanBayService.getFlights();
    }

    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<?> searchFlights(@RequestParam("query") String query) {
        return sanBayService.searchFlights(query);
    }
}
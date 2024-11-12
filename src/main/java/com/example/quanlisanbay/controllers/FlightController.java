package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import com.example.quanlisanbay.service.ChuyenBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FlightController {

    @Autowired
    private ChuyenBayService chuyenBayService;

    @PostMapping("/them_cb")
    @LoginRequired
    public String themChuyenBay(
            @RequestParam("flight-id") String flightId,
            @RequestParam("departure-airport") String departureAirport,
            @RequestParam("arrival-airport") String arrivalAirport,
            @RequestParam("departure-time") String departureTime,
            @RequestParam("arrival-time") String arrivalTime,
            RedirectAttributes redirectAttributes) {
        
        return chuyenBayService.themChuyenBay(flightId, departureAirport, arrivalAirport,
                departureTime, arrivalTime, redirectAttributes);
    }

    @PostMapping("/sua_cb")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaChuyenBay(
            @RequestParam("flight-id") String flightId,
            @RequestParam("departure-airport") String departureAirport,
            @RequestParam("arrival-airport") String arrivalAirport,
            @RequestParam("departure-time") String departureTime,
            @RequestParam("arrival-time") String arrivalTime) {
        
        return chuyenBayService.suaChuyenBay(flightId, departureAirport, arrivalAirport,
                departureTime, arrivalTime);
    }

    @PostMapping("/xoa_cb/{flightId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaChuyenBay(@PathVariable String flightId) {
        return chuyenBayService.xoaChuyenBay(flightId);
    }
}
package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import com.example.quanlisanbay.service.LichBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class FlightScheduleController {

    @Autowired
    private LichBayService lichBayService;

    @PostMapping("/them_lich")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themLich(
            @RequestParam("flight-id") String flightId,
            @RequestParam("aircraft-id") String aircraftId) {
        return lichBayService.themLich(flightId, aircraftId);
    }

    @PostMapping("/sua_lich")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaLich(
            @RequestParam("flight-id") String flightId,
            @RequestParam("aircraft-id") String aircraftId) {
        return lichBayService.suaLich(flightId, aircraftId);
    }

    @PostMapping("/xoa_lich")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaLich(
            @RequestParam("flight-id") String flightId,
            @RequestParam("aircraft-id") String aircraftId) {
        return lichBayService.xoaLich(flightId, aircraftId);
    }
}
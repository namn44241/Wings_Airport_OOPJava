package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.service.FlightDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlightDetailController {

    @Autowired
    private FlightDetailService flightService;

    @GetMapping("/get_flight_details")
    public ResponseEntity<?> getFlightDetails(@RequestParam("flight_id") String flightId) {
        return flightService.getFlightDetails(flightId);
    }
}

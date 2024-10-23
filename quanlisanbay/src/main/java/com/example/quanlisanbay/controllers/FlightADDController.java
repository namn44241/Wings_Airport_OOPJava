package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.service.FlightADDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/them_cb") // Sửa đường dẫn tại đây
public class FlightADDController {

    @Autowired
    private FlightADDService flightADDService;

    @PostMapping
    public ResponseEntity<?> addFlight(
            @RequestParam String flightId,
            @RequestParam String departureAirport,
            @RequestParam String arrivalAirport,
            @RequestParam String departureTime,
            @RequestParam String arrivalTime) {

        // Kiểm tra sân bay đi và đến
        if (departureAirport.equals(arrivalAirport)) {
            return ResponseEntity.badRequest().body("Error: Departure and Arrival airports must be different.");
        }

        // Convert times from string to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime depTime;
        LocalDateTime arrTime;
        try {
            depTime = LocalDateTime.parse(departureTime, formatter);
            arrTime = LocalDateTime.parse(arrivalTime, formatter);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: Invalid date format. Please use yyyy-MM-dd'T'HH:mm.");
        }

        // Call service to add flight
        boolean success = flightADDService.addFlight(flightId, departureAirport, arrivalAirport, depTime, arrTime);

        if (success) {
            return ResponseEntity.ok("Flight added successfully");
        } else {
            return ResponseEntity.status(500).body("Error: Failed to add flight");
        }
    }
}

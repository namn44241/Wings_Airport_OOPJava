package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.model.FlightEDIT;
import com.example.quanlisanbay.service.FlightEDITService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sua_cb")  // Chuyển từ /api/flight thành /sua_cb
public class FlightEDITController {

    @Autowired
    private FlightEDITService flightEDITService;

    @PostMapping
    public ResponseEntity<?> updateFlight(@RequestBody FlightEDIT flightEDIT) {
        try {
            // Kiểm tra sân bay đi và đến
            if (flightEDIT.getDepartureAirport().equals(flightEDIT.getArrivalAirport())) {
                return ResponseEntity.badRequest().body("Error: Departure and Arrival airports must be different.");
            }

            // Gọi service để cập nhật chuyến bay
            boolean success = flightEDITService.updateFlight(
                    flightEDIT.getFlightId(),
                    flightEDIT.getDepartureAirport(),
                    flightEDIT.getArrivalAirport(),
                    flightEDIT.getDepartureTime(),
                    flightEDIT.getArrivalTime());

            if (success) {
                return ResponseEntity.ok("Flight updated successfully");
            } else {
                return ResponseEntity.status(500).body("Error: Failed to update flight");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
}

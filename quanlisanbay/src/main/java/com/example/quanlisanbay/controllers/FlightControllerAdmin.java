package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.model.FlightDELETE;
import com.example.quanlisanbay.model.FlightEDIT;
import com.example.quanlisanbay.service.FlightADDService;
import com.example.quanlisanbay.service.FlightDELETEService;
import com.example.quanlisanbay.service.FlightEDITService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class FlightControllerAdmin {

    @Autowired
    private FlightADDService flightADDService;

    @Autowired
    private FlightDELETEService flightDELETEService;

    @Autowired
    private FlightEDITService flightEDITService;

    // Thêm chuyến bay
    @PostMapping("/them_cb")
    public ResponseEntity<?> addFlight(
            @RequestParam String flightId,
            @RequestParam String departureAirport,
            @RequestParam String arrivalAirport,
            @RequestParam String departureTime,
            @RequestParam String arrivalTime) {
        // Kiểm tra xem các tham số có rỗng không
        if (flightId.isEmpty() || departureAirport.isEmpty() || arrivalAirport.isEmpty() ||
                departureTime.isEmpty() || arrivalTime.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: All parameters must be provided.");
        }

        // Kiểm tra sân bay đi và đến
        if (departureAirport.equals(arrivalAirport)) {
            return ResponseEntity.badRequest().body("Error: Departure and Arrival airports must be different.");
        }

        // Chuyển đổi chuỗi thời gian thành LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime depTime;
        LocalDateTime arrTime;

        try {
            depTime = LocalDateTime.parse(departureTime, formatter);
            arrTime = LocalDateTime.parse(arrivalTime, formatter);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: Invalid date format. Please use yyyy-MM-dd'T'HH:mm.");
        }

        // Gọi service để thêm chuyến bay
        boolean success = flightADDService.addFlight(flightId, departureAirport, arrivalAirport, depTime, arrTime);

        if (success) {
            return ResponseEntity.ok("Flight added successfully");
        } else {
            return ResponseEntity.status(500).body("Error: Failed to add flight");
        }
    }

    // Sửa chuyến bay
    @PostMapping("/sua_cb")
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

    // Xóa chuyến bay
    @PostMapping("/xoa_cb/")
    public ResponseEntity<?> deleteFlight(@RequestBody FlightDELETE flightDELETE) {
        // Gọi service xóa chuyến bay
        return flightDELETEService.deleteFlightById(flightDELETE.getFlightId());
    }
}

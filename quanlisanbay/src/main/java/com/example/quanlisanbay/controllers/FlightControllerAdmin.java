package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import com.example.quanlisanbay.model.FlightDELETE;
import com.example.quanlisanbay.model.FlightEDIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flights")
public class FlightControllerAdmin {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // API thêm chuyến bay
    @PostMapping("/them_cb")
    @LoginRequired
    public ResponseEntity<?> addFlight(@RequestBody FlightEDIT flightEDIT) {
        // Validate input
        if (flightEDIT.getFlightId() == null || flightEDIT.getDepartureAirport() == null ||
                flightEDIT.getArrivalAirport() == null || flightEDIT.getDepartureTime() == null ||
                flightEDIT.getArrivalTime() == null) {
            return ResponseEntity.badRequest().body("Error: All flight information must be provided.");
        }

        // Validate airports
        if (flightEDIT.getDepartureAirport().equals(flightEDIT.getArrivalAirport())) {
            return ResponseEntity.badRequest().body("Error: Departure and Arrival airports must be different.");
        }

        try {
            String sql = "INSERT INTO flights (flight_id, departure_airport, arrival_airport, departure_time, arrival_time) VALUES (?, ?, ?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(sql,
                    flightEDIT.getFlightId(),
                    flightEDIT.getDepartureAirport(),
                    flightEDIT.getArrivalAirport(),
                    flightEDIT.getDepartureTime(),
                    flightEDIT.getArrivalTime());

            if (rowsAffected > 0) {
                return ResponseEntity.ok("Flight added successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to add flight");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding flight: " + e.getMessage());
        }
    }

    // API sửa chuyến bay
    @PostMapping("/sua_cb")
    @LoginRequired
    public ResponseEntity<?> updateFlight(@RequestBody FlightEDIT flightEDIT) {
        // Validate flight ID
        if (flightEDIT.getFlightId() == null) {
            return ResponseEntity.badRequest().body("Error: Flight ID must be provided for updating.");
        }

        try {
            // Check if flight exists
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM flights WHERE flight_id = ?",
                    Integer.class,
                    flightEDIT.getFlightId());

            if (count == null || count == 0) {
                return ResponseEntity.status(404).body("Error: Flight not found with ID: " + flightEDIT.getFlightId());
            }

            // Validate airports
            if (flightEDIT.getDepartureAirport().equals(flightEDIT.getArrivalAirport())) {
                return ResponseEntity.badRequest().body("Error: Departure and Arrival airports must be different.");
            }

            // Update flight
            String updateSql = "UPDATE flights SET departure_airport = ?, arrival_airport = ?, departure_time = ?, arrival_time = ? WHERE flight_id = ?";
            int rowsAffected = jdbcTemplate.update(updateSql,
                    flightEDIT.getDepartureAirport(),
                    flightEDIT.getArrivalAirport(),
                    flightEDIT.getDepartureTime(),
                    flightEDIT.getArrivalTime(),
                    flightEDIT.getFlightId());

            if (rowsAffected > 0) {
                return ResponseEntity.ok("Flight updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to update flight");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating flight: " + e.getMessage());
        }
    }

    // API xóa chuyến bay
    @PostMapping("/xoa_cb/")
    @LoginRequired
    public ResponseEntity<?> deleteFlight(@RequestBody FlightDELETE flightDELETE) {
        if (flightDELETE.getFlightId() == null) {
            return ResponseEntity.badRequest().body("Error: Flight ID must be provided for deleting.");
        }

        try {
            // Kiểm tra xem chuyến bay có đang được sử dụng không
            Integer usageCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM bookings WHERE flight_id = ?",
                    Integer.class,
                    flightDELETE.getFlightId());

            if (usageCount != null && usageCount > 0) {
                return ResponseEntity.badRequest().body("Error: Flight cannot be deleted as it is in use.");
            }

            // Xóa chuyến bay
            int rowsAffected = jdbcTemplate.update(
                    "DELETE FROM flights WHERE flight_id = ?",
                    flightDELETE.getFlightId());

            if (rowsAffected > 0) {
                return ResponseEntity
                        .ok("Flight with ID " + flightDELETE.getFlightId() + " has been deleted successfully");
            } else {
                return ResponseEntity.status(404).body("Flight not found with ID: " + flightDELETE.getFlightId());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting flight: " + e.getMessage());
        }
    }
}
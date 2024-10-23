package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.model.ErrorResponse;
import com.example.quanlisanbay.model.FlightDTO;
import com.example.quanlisanbay.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/flights")
    public ResponseEntity<?> getFlights() {
        try {
            List<FlightDTO> flights = flightService.getAllFlights();
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while fetching flight data", e.getMessage()));
        }
    }
}

package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.model.FlightSearchDTO;
import com.example.quanlisanbay.service.FlightSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FlightSearchController {

    @Autowired
    private FlightSearchService flightSearchService;

    @GetMapping("/search")
    public ResponseEntity<?> searchFlights(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "type") String searchType) {
        try {
            List<FlightSearchDTO> flights = flightSearchService.searchFlights(query, searchType);
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while searching flights");
        }
    }
}

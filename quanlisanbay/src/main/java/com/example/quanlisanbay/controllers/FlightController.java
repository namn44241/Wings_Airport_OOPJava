package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.model.Booking;
import com.example.quanlisanbay.model.ErrorResponse;
import com.example.quanlisanbay.model.FlightDTO;
import com.example.quanlisanbay.model.FlightSearchDTO;
import com.example.quanlisanbay.service.BookingService;
import com.example.quanlisanbay.service.FlightDetailService;
import com.example.quanlisanbay.service.FlightSearchService;
import com.example.quanlisanbay.service.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private BookingService bookingService;

    @Autowired
    private FlightSearchService flightSearchService;

    @Autowired
    private FlightDetailService flightDetailService;

    private static final Logger logger = LoggerFactory.getLogger(FlightController.class);

    @GetMapping("/flights")
    public ResponseEntity<?> getFlights() {
        return fetchFlights();
    }

    private ResponseEntity<?> fetchFlights() {
        try {
            List<FlightDTO> flights = flightService.getAllFlights();
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse("An error occurred while fetching flight data", e.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookFlight(@RequestBody Booking booking) {
        logger.info("Received booking: {}", booking);

        if (booking.getCustomerId() == null || booking.getFlightId() == null || booking.getDepartureDate() == null) {
            logger.error("Invalid data: {}", booking);
            return ResponseEntity.status(400).body("{\"success\": false, \"error\": \"Invalid data\"}");
        }

        boolean success = bookingService.bookFlight(booking);
        if (success) {
            logger.info("Booking successful: {}", booking);
            return ResponseEntity.ok().body("{\"success\": true}");
        } else {
            logger.error("Booking failed for: {}", booking);
            return ResponseEntity.status(500).body("{\"success\": false, \"error\": \"Booking failed\"}");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFlights(@RequestParam(name = "query") String query,
                                           @RequestParam(name = "type") String searchType) {
        try {
            List<FlightSearchDTO> flights = flightSearchService.searchFlights(query, searchType);
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while searching flights");
        }
    }

    @GetMapping("/get_flight_details")
    public ResponseEntity<?> getFlightDetails(@RequestParam("flight_id") String flightId) {
        return flightDetailService.getFlightDetails(flightId);
    }
}

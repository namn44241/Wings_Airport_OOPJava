package com.example.quanlisanbay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.quanlisanbay.model.Booking;
import com.example.quanlisanbay.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/book")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    // Phương thức gọi service để thực hiện đặt chỗ
    @PostMapping
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

}

package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.model.FlightDELETE;
import com.example.quanlisanbay.service.FlightDELETEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/xoa_cb") // Đổi từ /api/flights thành /xoa_cb
public class FlightDELETEController {

    @Autowired
    private FlightDELETEService flightDELETEService;

    // Xóa chuyến bay với dữ liệu từ FlightDELETE model
    @PostMapping("/delete")
    public ResponseEntity<?> deleteFlight(@RequestBody FlightDELETE flightDELETE) {
        // Sử dụng phương thức xóa chuyến bay trong service
        return flightDELETEService.deleteFlightById(flightDELETE.getFlightId());
    }
}

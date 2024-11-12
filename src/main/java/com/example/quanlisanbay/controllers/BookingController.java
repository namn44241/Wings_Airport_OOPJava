package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;         
import org.slf4j.LoggerFactory;   

import java.time.LocalDate;
import com.example.quanlisanbay.service.DatChoService;

@Controller
public class BookingController {

    @Autowired
    private DatChoService datChoService;
    
    @Autowired

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    @PostMapping("/them_dat_cho")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themDatCho(
            @RequestParam("customer-id") String customerId,
            @RequestParam("flight-id") String flightId,
            @RequestParam("departure-datetime") String departureDatetime
    ) {
        try {
            LocalDateTime departureDate = LocalDateTime.parse(departureDatetime);
            LocalDate ngayDi = departureDate.toLocalDate();
            return datChoService.themDatCho(customerId, flightId, ngayDi);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi thêm đặt chỗ: " + e.getMessage()));
        }
    }

    @PostMapping("/them_dat_cho_fe")
    @ResponseBody
    public ResponseEntity<?> themDatChoFe(
            @RequestParam(value = "customer-id") String customerId,
            @RequestParam(value = "flight-id") String flightId,
            @RequestParam(value = "departure-datetime") String departureDatetime,
            @RequestParam(value = "customer-email") String customerEmail
    ) {
        try {
            LocalDateTime departureDate = LocalDateTime.parse(departureDatetime);
            LocalDate ngayDi = departureDate.toLocalDate();
            return datChoService.themDatChoFe(customerId, flightId, ngayDi, customerEmail);
        } catch (Exception e) {
            log.error("Error in booking process: ", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi không xác định: " + e.getMessage()));
        }
    }

    @PostMapping("/sua_dat_cho")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaDatCho(
            @RequestParam("customer-id") String customerId,
            @RequestParam("flight-id") String newFlightId,
            @RequestParam("departure-datetime") String newDepartureDatetime
    ) {
        try {
            LocalDateTime newNgayDi = LocalDateTime.parse(newDepartureDatetime);
            LocalDate ngayDi = newNgayDi.toLocalDate();
            return datChoService.suaDatCho(customerId, newFlightId, ngayDi);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi cập nhật đặt chỗ: " + e.getMessage()));
        }
    }

    @PostMapping("/xoa_dat_cho")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaDatCho(
        @RequestParam("customer_id") String customerId,
        @RequestParam("flight_id") String flightId,
        @RequestParam("departure_date") String departureDate
    ) {
        try {
            LocalDate ngayDi = LocalDate.parse(departureDate);
            return datChoService.xoaDatCho(customerId, flightId, ngayDi);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi xóa đặt chỗ: " + e.getMessage()));
        }
    }
}
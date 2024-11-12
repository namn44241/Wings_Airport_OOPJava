package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import com.example.quanlisanbay.service.PhanCongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AssignmentController {

    @Autowired
    private PhanCongService phanCongService;

    @PostMapping("/them_phan_cong")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themPhanCong(
            @RequestParam("employee-id") String employeeId,
            @RequestParam("flight-id") String flightId,
            @RequestParam("departure-datetime") String departureDatetime
    ) {
        try {
            LocalDateTime departureDate = LocalDateTime.parse(departureDatetime); 
            LocalDate ngayDi = departureDate.toLocalDate();
            
            return phanCongService.themPhanCong(employeeId, flightId, ngayDi);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Lỗi khi thêm phân công: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/sua_phan_cong")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaPhanCong(
            @RequestParam("employee-id") String employeeId,
            @RequestParam("flight-id") String newFlightId,
            @RequestParam("departure-datetime") String newDepartureDatetime
    ) {
        try {
            LocalDateTime newNgayDi = LocalDateTime.parse(newDepartureDatetime);
            LocalDate ngayDi = newNgayDi.toLocalDate();
            
            return phanCongService.suaPhanCong(employeeId, newFlightId, ngayDi);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Lỗi khi cập nhật phân công: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/xoa_phan_cong")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaPhanCong(
            @RequestParam("employee_id") String employeeId,
            @RequestParam("flight_id") String flightId,
            @RequestParam("departure_date") String departureDate
    ) {
        try {
            LocalDate ngayDi = LocalDate.parse(departureDate);
            return phanCongService.xoaPhanCong(employeeId, flightId, ngayDi);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Lỗi khi xóa phân công: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/get_flight_details_for_assignment")
    @ResponseBody
    public ResponseEntity<?> getFlightDetailsForAssignment(@RequestParam("flight_id") String flightId) {
        try {
            return phanCongService.getFlightDetailsForAssignment(flightId);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Lỗi khi lấy thông tin chuyến bay: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
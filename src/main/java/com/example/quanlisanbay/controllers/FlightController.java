package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FlightController {

   @Autowired
   private JdbcTemplate jdbcTemplate;

   @PostMapping("/them_cb")
   @LoginRequired
   public String themChuyenBay(
           @RequestParam("flight-id") String flightId,
           @RequestParam("departure-airport") String departureAirport,
           @RequestParam("arrival-airport") String arrivalAirport,
           @RequestParam("departure-time") String departureTime,
           @RequestParam("arrival-time") String arrivalTime,
           RedirectAttributes redirectAttributes) {

       if (departureAirport.equals(arrivalAirport)) {
           redirectAttributes.addFlashAttribute("error", "Lỗi: Sân bay đi và đến phải khác nhau");
           return "redirect:/admin";
       }

       LocalDateTime departureDateTime = LocalDateTime.parse(departureTime);
       LocalDateTime arrivalDateTime = LocalDateTime.parse(arrivalTime);

       String query = "INSERT INTO ChuyenBay (MaChuyenBay, TenSanBayDi, TenSanBayDen, GioDi, GioDen) VALUES (?, ?, ?, ?, ?)";
       jdbcTemplate.update(query, flightId, departureAirport, arrivalAirport, departureDateTime, arrivalDateTime);
       
       redirectAttributes.addFlashAttribute("success", "Thêm chuyến bay thành công");
       return "redirect:/admin";
   }

   @PostMapping("/sua_cb")
   @LoginRequired
   @ResponseBody
   public ResponseEntity<?> suaChuyenBay(
           @RequestParam("flight-id") String flightId,
           @RequestParam("departure-airport") String departureAirport, 
           @RequestParam("arrival-airport") String arrivalAirport,
           @RequestParam("departure-time") String departureTime,
           @RequestParam("arrival-time") String arrivalTime) {

       if (departureAirport.equals(arrivalAirport)) {
           Map<String, String> response = new HashMap<>();
           response.put("status", "error");
           response.put("message", "Lỗi: Sân bay đi và đến phải khác nhau");
           return ResponseEntity.badRequest().body(response);
       }

       try {
           LocalDateTime departureDateTime = LocalDateTime.parse(departureTime);
           LocalDateTime arrivalDateTime = LocalDateTime.parse(arrivalTime);

           String query = "UPDATE ChuyenBay SET TenSanBayDi = ?, TenSanBayDen = ?, GioDi = ?, GioDen = ? WHERE MaChuyenBay = ?";
           jdbcTemplate.update(query, departureAirport, arrivalAirport, departureDateTime, arrivalDateTime, flightId);

           Map<String, String> response = new HashMap<>();
           response.put("status", "success"); 
           response.put("message", "Cập nhật thông tin chuyến bay thành công");
           return ResponseEntity.ok(response);

       } catch (Exception e) {
           Map<String, String> response = new HashMap<>();
           response.put("status", "error");
           response.put("message", "Lỗi khi cập nhật thông tin chuyến bay: " + e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
       }
   }

   @PostMapping("/xoa_cb/{flightId}")
   @LoginRequired
   @ResponseBody
   public ResponseEntity<?> xoaChuyenBay(@PathVariable String flightId) {
       try {
           String checkQuery = """
               SELECT COUNT(*) 
               FROM (
                   SELECT MaChuyenBay FROM LichBay WHERE MaChuyenBay = ?
                   UNION ALL
                   SELECT MaChuyenBay FROM DatCho WHERE MaChuyenBay = ?
                   UNION ALL 
                   SELECT MaChuyenBay FROM PhanCong WHERE MaChuyenBay = ?
               ) AS UsedFlights
               """;

           int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, flightId, flightId, flightId);

           if (count > 0) {
               Map<String, String> response = new HashMap<>();
               response.put("status", "error");
               response.put("message", "Chuyến bay này đã được dùng để đặt Lịch bay, Đặt chỗ hoặc được Phân công nên không thể xóa!");
               return ResponseEntity.badRequest().body(response);
           } else {
               String deleteQuery = "DELETE FROM ChuyenBay WHERE MaChuyenBay = ?";
               jdbcTemplate.update(deleteQuery, flightId);

               Map<String, String> response = new HashMap<>();
               response.put("status", "success");
               response.put("message", "Đã xóa chuyến bay thành công");
               return ResponseEntity.ok(response);
           }

       } catch (Exception e) {
           Map<String, String> response = new HashMap<>();
           response.put("status", "error"); 
           response.put("message", "Có lỗi xảy ra: " + e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
       }
   }
}
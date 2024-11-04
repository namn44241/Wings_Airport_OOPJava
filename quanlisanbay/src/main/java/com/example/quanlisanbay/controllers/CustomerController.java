package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
@Controller
public class CustomerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/them_kh")
    @LoginRequired
    public String themKhachHang(
            @RequestParam("customer-id") String customerId,
            @RequestParam("customer-phone") String customerPhone,
            @RequestParam("customer-last-name") String customerLastName,
            @RequestParam("customer-first-name") String customerFirstName,
            @RequestParam("customer-address") String customerAddress) {
            
        String query = "INSERT INTO KhachHang (MaKH, SDT, HoDem, Ten, DiaChi) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, customerId, customerPhone, customerLastName, customerFirstName, customerAddress);
        return "redirect:/admin";
    }

    @GetMapping("/next_customer_id")
    @ResponseBody
    public ResponseEntity<?> getNextCustomerId() {
        String query = "SELECT MAX(MaKH) FROM KhachHang";
        String maxCustomerId = jdbcTemplate.queryForObject(query, String.class);
        
        String nextCustomerId;
        if (maxCustomerId != null) {
            int maxIdNum = Integer.parseInt(maxCustomerId.substring(2));
            nextCustomerId = String.format("KH%06d", maxIdNum + 1);
        } else {
            nextCustomerId = "KH000001";
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("next_customer_id", nextCustomerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/them_kh_fe")
    @ResponseBody
    public ResponseEntity<?> themKhachHangFe(
            @RequestParam("customer-id") String customerId,
            @RequestParam("customer-phone") String customerPhone,
            @RequestParam("customer-last-name") String customerLastName,
            @RequestParam("customer-first-name") String customerFirstName,
            @RequestParam("customer-address") String customerAddress) {
        
        try {
            String query = "INSERT INTO KhachHang (MaKH, SDT, HoDem, Ten, DiaChi) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(query, customerId, customerPhone, customerLastName, customerFirstName, customerAddress);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Khách hàng đã được thêm thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/sua_kh")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaKhachHang(
            @RequestParam("customer-id") String customerId,
            @RequestParam("customer-phone") String customerPhone,
            @RequestParam("customer-last-name") String customerLastName,
            @RequestParam("customer-first-name") String customerFirstName,
            @RequestParam("customer-address") String customerAddress) {
        
        try {
            String query = "UPDATE KhachHang SET SDT = ?, HoDem = ?, Ten = ?, DiaChi = ? WHERE MaKH = ?";
            jdbcTemplate.update(query, customerPhone, customerLastName, customerFirstName, customerAddress, customerId);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cập nhật thông tin khách hàng thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Lỗi khi cập nhật thông tin khách hàng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/xoa_kh/{customerId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaKhachHang(@PathVariable String customerId) {
        try {
            String checkQuery = "SELECT COUNT(*) FROM DatCho WHERE MaKH = ?";
            int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, customerId);
            
            if (count > 0) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Không thể xóa khách hàng này vì khách hàng này đã đặt chỗ!");
                return ResponseEntity.badRequest().body(response);
            } else {
                String deleteQuery = "DELETE FROM KhachHang WHERE MaKH = ?";
                jdbcTemplate.update(deleteQuery, customerId);
                
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Đã xóa khách hàng thành công.");
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

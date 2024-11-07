package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final List<String> VALID_EMPLOYEE_TYPES = Arrays.asList("Tiếp viên", "Phi công");

    @PostMapping("/them_nv")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themNhanVien(
            @RequestParam("employee-id") String employeeId,
            @RequestParam("employee-last-name") String employeeLastName,
            @RequestParam("employee-first-name") String employeeFirstName,
            @RequestParam("employee-phone") String employeePhone,
            @RequestParam("employee-address") String employeeAddress,
            @RequestParam("employee-salary") String employeeSalary,
            @RequestParam("employee-type") String employeeType) {
        
        if (!VALID_EMPLOYEE_TYPES.contains(employeeType)) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Loại nhân viên không hợp lệ");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String query = "INSERT INTO NhanVien (MaNV, HoDem, Ten, SDT, DiaChi, Luong, LoaiNV) VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(query, employeeId, employeeLastName, employeeFirstName, employeePhone, 
                              employeeAddress, employeeSalary, employeeType);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Thêm nhân viên thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Lỗi khi thêm nhân viên: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/sua_nv/{employeeId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaNhanVien(
            @PathVariable String employeeId,
            @RequestParam("employee-last-name") String employeeLastName,
            @RequestParam("employee-first-name") String employeeFirstName,
            @RequestParam("employee-phone") String employeePhone,
            @RequestParam("employee-address") String employeeAddress,
            @RequestParam("employee-salary") String employeeSalary,
            @RequestParam("employee-type") String employeeType) {

        if (!VALID_EMPLOYEE_TYPES.contains(employeeType)) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Loại nhân viên không hợp lệ");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Double.parseDouble(employeeSalary);
        } catch (NumberFormatException e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Lương phải là một số hợp lệ");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String query = "UPDATE NhanVien SET HoDem = ?, Ten = ?, SDT = ?, DiaChi = ?, Luong = ?, LoaiNV = ? WHERE MaNV = ?";
            jdbcTemplate.update(query, employeeLastName, employeeFirstName, employeePhone, 
                              employeeAddress, employeeSalary, employeeType, employeeId);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cập nhật thông tin nhân viên thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Lỗi khi cập nhật thông tin nhân viên: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/xoa_nv/{employeeId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaNhanVien(@PathVariable String employeeId) {
        try {
            String checkQuery = "SELECT COUNT(*) FROM PhanCong WHERE MaNV = ?";
            int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, employeeId);
            
            if (count > 0) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Không thể xóa nhân viên này vì đã được phân công.");
                return ResponseEntity.badRequest().body(response);
            } else {
                String deleteQuery = "DELETE FROM NhanVien WHERE MaNV = ?";
                jdbcTemplate.update(deleteQuery, employeeId);
                
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Đã xóa nhân viên thành công.");
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
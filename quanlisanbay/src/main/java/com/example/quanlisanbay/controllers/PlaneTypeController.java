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
public class PlaneTypeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/them_loai_mb")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themLoaiMayBay(
            @RequestParam("plane-type-id") String planeTypeId,
            @RequestParam("manufacturer") String manufacturer) {
        
        try {
            String query = "INSERT INTO LoaiMayBay (MaLoai, HangSanXuat) VALUES (?, ?)";
            jdbcTemplate.update(query, planeTypeId, manufacturer);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Thêm loại máy bay thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Lỗi khi thêm loại máy bay: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/sua_loai_mb/{planeTypeId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaLoaiMayBay(
            @PathVariable String planeTypeId,
            @RequestParam("manufacturer") String manufacturer) {
        
        try {
            String query = "UPDATE LoaiMayBay SET HangSanXuat = ? WHERE MaLoai = ?";
            jdbcTemplate.update(query, manufacturer, planeTypeId);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cập nhật thông tin loại máy bay thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Lỗi khi cập nhật thông tin loại máy bay: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/xoa_loai_mb/{planeTypeId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaLoaiMayBay(@PathVariable String planeTypeId) {
        try {
            String checkQuery = """
                SELECT COUNT(*)
                FROM (
                    SELECT MaLoai FROM MayBay WHERE MaLoai = ?
                    UNION ALL
                    SELECT MaLoai FROM LichBay WHERE MaLoai = ?
                ) AS UsedTypes
                """;
            
            int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, planeTypeId, planeTypeId);
            
            if (count > 0) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Loại máy bay này đã được sử dụng trong bảng Máy bay hoặc Lịch bay");
                return ResponseEntity.badRequest().body(response);
            } else {
                String deleteQuery = "DELETE FROM LoaiMayBay WHERE MaLoai = ?";
                jdbcTemplate.update(deleteQuery, planeTypeId);
                
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Đã xóa loại máy bay thành công");
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
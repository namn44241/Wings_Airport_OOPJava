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
public class PlaneController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/them_mb")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themMayBay(
            @RequestParam("plane-id") String planeId,
            @RequestParam("plane-type-id") String planeTypeId,
            @RequestParam("seat-quantity") String seatQuantity) {
        
        try {
            // Kiểm tra độ dài của plane_id
            if (planeId.length() > 10) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Số hiệu máy bay không được vượt quá 10 ký tự");
                return ResponseEntity.badRequest().body(response);
            }

            // Kiểm tra máy bay đã tồn tại
            String checkQuery = "SELECT COUNT(*) FROM MayBay WHERE SoHieu = ?";
            int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, planeId);
            if (count > 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Máy bay với số hiệu này đã tồn tại!");
                return ResponseEntity.badRequest().body(response);
            }

            // Kiểm tra loại máy bay tồn tại
            String checkTypeQuery = "SELECT COUNT(*) FROM LoaiMayBay WHERE MaLoai = ?";
            count = jdbcTemplate.queryForObject(checkTypeQuery, Integer.class, planeTypeId);
            if (count == 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Loại máy bay không tồn tại!");
                return ResponseEntity.badRequest().body(response);
            }

            String query = "INSERT INTO MayBay (SoHieu, MaLoai, SoGheNgoi) VALUES (?, ?, ?)";
            jdbcTemplate.update(query, planeId, planeTypeId, seatQuantity);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Thêm máy bay thành công");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi thêm máy bay: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/sua_mb")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaMayBay(
            @RequestParam("plane-id") String planeId,
            @RequestParam("plane-type-id") String planeTypeId,
            @RequestParam("seat-quantity") String seatQuantity) {
        
        try {
            // Kiểm tra máy bay tồn tại
            String checkPlaneQuery = "SELECT COUNT(*) FROM MayBay WHERE SoHieu = ?";
            int count = jdbcTemplate.queryForObject(checkPlaneQuery, Integer.class, planeId);
            if (count == 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Máy bay không tồn tại!");
                return ResponseEntity.badRequest().body(response);
            }

            // Kiểm tra máy bay trong lịch bay
            String checkScheduleQuery = "SELECT COUNT(*) FROM LichBay WHERE SoHieu = ?";
            count = jdbcTemplate.queryForObject(checkScheduleQuery, Integer.class, planeId);
            if (count > 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Không thể sửa thông tin máy bay này vì đã được sử dụng trong lịch bay!");
                return ResponseEntity.badRequest().body(response);
            }

            String updateQuery = "UPDATE MayBay SET MaLoai = ?, SoGheNgoi = ? WHERE SoHieu = ?";
            jdbcTemplate.update(updateQuery, planeTypeId, seatQuantity, planeId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cập nhật thông tin máy bay thành công");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi cập nhật máy bay: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/xoa_mb/{planeId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaMayBay(@PathVariable String planeId) {
        try {
            // Kiểm tra máy bay trong lịch bay
            String checkScheduleQuery = "SELECT COUNT(*) FROM LichBay WHERE SoHieu = ?";
            int scheduleCount = jdbcTemplate.queryForObject(checkScheduleQuery, Integer.class, planeId);

            // Kiểm tra máy bay trong loại máy bay
            String checkTypeQuery = "SELECT COUNT(*) FROM LoaiMayBay WHERE MaLoai IN (SELECT MaLoai FROM MayBay WHERE SoHieu = ?)";
            int typeCount = jdbcTemplate.queryForObject(checkTypeQuery, Integer.class, planeId);

            if (scheduleCount > 0 || typeCount > 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Máy bay đang được sử dụng trong bảng Lịch bay hoặc Loại máy bay");
                return ResponseEntity.badRequest().body(response);
            }

            String deleteQuery = "DELETE FROM MayBay WHERE SoHieu = ?";
            jdbcTemplate.update(deleteQuery, planeId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã xóa máy bay " + planeId + " thành công.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi xóa máy bay: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
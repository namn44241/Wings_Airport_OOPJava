package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Map;

@RestController
public class PhanCongController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/them_phan_cong")
    @LoginRequired
    public ResponseEntity<?> themPhanCong(@RequestBody Map<String, String> request) {
        try {
            String employeeId = request.get("employee-id");
            String flightId = request.get("flight-id");
            LocalDateTime departureDateTime = LocalDateTime.parse(request.get("departure-datetime"));
            LocalDate ngayDi = departureDateTime.toLocalDate();

            // Kiểm tra phân công đã tồn tại
            String checkQuery = "SELECT COUNT(*) FROM PhanCong WHERE MaNV = ? AND MaChuyenBay = ? AND NgayDi = ?";
            int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, employeeId, flightId, ngayDi);
            
            if (count > 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Đã tồn tại phân công cho nhân viên này trên chuyến bay này."));
            }

            // Thêm bản ghi mới
            String insertQuery = "INSERT INTO PhanCong (MaNV, NgayDi, MaChuyenBay) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertQuery, employeeId, ngayDi, flightId);
            
            return ResponseEntity.ok(Map.of("success", "Thêm phân công thành công"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi thêm phân công: " + e.getMessage()));
        }
    }
}
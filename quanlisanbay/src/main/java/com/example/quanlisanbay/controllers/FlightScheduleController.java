package com.example.quanlisanbay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.quanlisanbay.config.LoginRequired;

import java.util.HashMap;
import java.util.Map;

@Controller
public class FlightScheduleController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/them_lich")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themLich(
            @RequestParam("flight-id") String flightId,
            @RequestParam("aircraft-id") String aircraftId
    ) {
        try {
            // Lấy GioDi từ bảng ChuyenBay
            String queryGetGiodi = "SELECT GioDi FROM ChuyenBay WHERE MaChuyenBay = ?";
            Map<String, Object> result = jdbcTemplate.queryForMap(queryGetGiodi, flightId);

            if (result != null) {
                String gioDi = result.get("GioDi").toString();
                String ngayDi = gioDi.split(" ")[0];  // Lấy phần ngày

                // Lấy MaLoai từ bảng MayBay
                String queryGetMaloai = "SELECT MaLoai FROM MayBay WHERE SoHieu = ?";
                String maLoai = jdbcTemplate.queryForObject(queryGetMaloai, String.class, aircraftId);

                if (maLoai != null) {
                    // Kiểm tra trùng lặp
                    String checkQuery = "SELECT COUNT(*) FROM LichBay WHERE NgayDi = ? AND MaChuyenBay = ?";
                    int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, ngayDi, flightId);

                    if (count > 0) {
                        Map<String, String> response = new HashMap<>();
                        response.put("error", "NgayDi và MaChuyenBay đã bị trùng lặp");
                        return ResponseEntity.badRequest().body(response);
                    }

                    // Thêm lịch bay mới
                    String queryInsert = "INSERT INTO LichBay (NgayDi, MaChuyenBay, SoHieu, MaLoai) VALUES (?, ?, ?, ?)";
                    jdbcTemplate.update(queryInsert, ngayDi, flightId, aircraftId, maLoai);

                    Map<String, String> response = new HashMap<>();
                    response.put("success", "Thêm lịch bay thành công");
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("error", "Không tìm thấy MaLoai cho máy bay này");
                    return ResponseEntity.badRequest().body(response);
                }
            }
            return ResponseEntity.badRequest().body("Không tìm thấy chuyến bay");

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Lỗi khi thêm lịch bay: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/sua_lich")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaLich(
            @RequestParam("flight-id") String flightId,
            @RequestParam("aircraft-id") String aircraftId
    ) {
        try {
            // Kiểm tra tồn tại
            String checkQuery = "SELECT COUNT(*) FROM LichBay WHERE MaChuyenBay = ?";
            int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, flightId);

            if (count == 0) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Lịch bay không tồn tại");
                return ResponseEntity.badRequest().body(response);
            }

            // Cập nhật lịch bay
            String updateQuery = "UPDATE LichBay SET SoHieu = ? WHERE MaChuyenBay = ?";
            jdbcTemplate.update(updateQuery, aircraftId, flightId);

            Map<String, String> response = new HashMap<>();
            response.put("success", "Cập nhật lịch bay thành công");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Lỗi khi cập nhật lịch bay: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/xoa_lich")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaLich(
            @RequestParam("flight-id") String flightId,
            @RequestParam("aircraft-id") String aircraftId
    ) {
        try {
            // Kiểm tra ràng buộc với DatCho
            String checkDatChoQuery = "SELECT COUNT(*) FROM DatCho WHERE MaChuyenBay = ?";
            int datChoCount = jdbcTemplate.queryForObject(checkDatChoQuery, Integer.class, flightId);

            // Kiểm tra ràng buộc với PhanCong 
            String checkPhanCongQuery = "SELECT COUNT(*) FROM PhanCong WHERE MaChuyenBay = ?";
            int phanCongCount = jdbcTemplate.queryForObject(checkPhanCongQuery, Integer.class, flightId);

            if (datChoCount > 0 || phanCongCount > 0) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Không thể xóa. Mã chuyến bay đã được sử dụng trong bảng DatCho hoặc PhanCong.");
                return ResponseEntity.badRequest().body(response);
            }

            // Thực hiện xóa
            String deleteQuery = "DELETE FROM LichBay WHERE MaChuyenBay = ? AND SoHieu = ?";
            jdbcTemplate.update(deleteQuery, flightId, aircraftId);

            Map<String, String> response = new HashMap<>();
            response.put("success", "Xóa lịch bay thành công");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Lỗi khi xóa lịch bay: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
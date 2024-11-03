package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.model.PlaneType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlaneTypeController {

    @Autowired
    private JdbcTemplate jdbcTemplate; // Chỉ cần khởi tạo JdbcTemplate ở đây

    // API thêm loại máy bay
    @PostMapping("/them_loai_mb")
    public ResponseEntity<Map<String, String>> addPlaneType(@Valid @RequestBody PlaneType planeType) {
        Map<String, String> response = new HashMap<>();
        try {
            String query = "INSERT INTO LoaiMayBay (MaLoai, HangSanXuat) VALUES (?, ?)";
            jdbcTemplate.update(query, planeType.getPlaneTypeId(), planeType.getManufacturer());

            response.put("status", "success");
            response.put("message", "Thêm loại máy bay thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // API xóa loại máy bay
    @DeleteMapping("/xoa_loai_mb/{planeTypeId}")
    public ResponseEntity<String> deletePlaneType(@PathVariable String planeTypeId) {
        try {
            String query = "DELETE FROM LoaiMayBay WHERE MaLoai = ?";
            int rowsAffected = jdbcTemplate.update(query, planeTypeId);
            if (rowsAffected > 0) {
                return ResponseEntity.ok("Loại máy bay đã được xóa thành công");
            } else {
                return ResponseEntity.status(404).body("Không tìm thấy loại máy bay với ID: " + planeTypeId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Không thể xóa loại máy bay: " + e.getMessage());
        }
    }

    // API cập nhật loại máy bay
    @PutMapping("/sua_loai_mb/{planeTypeId}")
    public ResponseEntity<?> updatePlaneType(
            @PathVariable("planeTypeId") String planeTypeId,
            @RequestParam("manufacturer") String manufacturer) {
        try {
            String query = "UPDATE LoaiMayBay SET HangSanXuat = ? WHERE MaLoai = ?";
            int rowsAffected = jdbcTemplate.update(query, manufacturer, planeTypeId);
            if (rowsAffected > 0) {
                return ResponseEntity.ok("Cập nhật thông tin loại máy bay thành công");
            } else {
                return ResponseEntity.status(404).body("Không tìm thấy loại máy bay với ID: " + planeTypeId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi cập nhật thông tin loại máy bay: " + e.getMessage());
        }
    }

    // API lấy danh sách tất cả các loại máy bay
    @GetMapping("/get_plane_types")
    public List<PlaneType> getAllPlaneTypes() {
        String query = "SELECT MaLoai AS planeTypeId, HangSanXuat AS manufacturer FROM LoaiMayBay";
        return jdbcTemplate.query(query,
                (rs, rowNum) -> new PlaneType(rs.getString("planeTypeId"), rs.getString("manufacturer")));
    }

    // API lấy mã loại máy bay tiếp theo
    @GetMapping("/next_plane_type_id")
    public Map<String, String> getNextPlaneTypeId() {
        String query = "SELECT MAX(MaLoai) FROM LoaiMayBay";
        Integer maxPlaneTypeId = jdbcTemplate.queryForObject(query, Integer.class);

        Map<String, String> response = new HashMap<>();
        if (maxPlaneTypeId != null) {
            int nextIdNum = maxPlaneTypeId + 1;
            response.put("nextPlaneTypeId", String.format("%02d", nextIdNum)); // Đảm bảo mã loại có 2 chữ số
        } else {
            response.put("nextPlaneTypeId", "01"); // Trả về "01" nếu không có loại máy bay nào
        }
        return response;
    }
}

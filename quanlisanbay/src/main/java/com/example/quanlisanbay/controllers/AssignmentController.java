package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AssignmentController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/them_phan_cong")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themPhanCong(
            @RequestParam("employee-id") String employeeId,
            @RequestParam("flight-id") String flightId,
            @RequestParam("departure-datetime") String departureDatetime
    ) {
        try {
            // Chuyển đổi chuỗi ngày tháng thành đối tượng LocalDateTime
            LocalDateTime departureDate = LocalDateTime.parse(departureDatetime); 
            String ngayDi = departureDate.toLocalDate().toString(); // Chỉ lấy phần date

            // Kiểm tra phân công đã tồn tại
            String checkQuery = "SELECT COUNT(*) FROM PhanCong WHERE MaNV = ? AND MaChuyenBay = ? AND NgayDi = ?";
            int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, employeeId, flightId, ngayDi);

            if (count > 0) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Đã tồn tại phân công cho nhân viên này trên chuyến bay này.");
                return ResponseEntity.badRequest().body(response);
            }

            // Thêm bản ghi mới
            String insertQuery = "INSERT INTO PhanCong (MaNV, NgayDi, MaChuyenBay) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertQuery, employeeId, ngayDi, flightId);

            Map<String, String> response = new HashMap<>();
            response.put("success", "Thêm phân công thành công");
            return ResponseEntity.ok(response);

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
            LocalDateTime newNgayDi = LocalDateTime.parse(newDepartureDatetime).toLocalDate().atStartOfDay();

            // Lấy thông tin phân công hiện tại
            String getCurrentAssignmentQuery = 
                "SELECT NgayDi, MaChuyenBay FROM PhanCong WHERE MaNV = ?";
            Map<String, Object> current = jdbcTemplate.queryForMap(getCurrentAssignmentQuery, employeeId);

            if (current == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Không tìm thấy phân công để cập nhật");
                return ResponseEntity.notFound().build();
            }

            // Cập nhật phân công
            String updateQuery = 
                "UPDATE PhanCong SET NgayDi = ?, MaChuyenBay = ? " +
                "WHERE MaNV = ? AND NgayDi = ? AND MaChuyenBay = ?";
            int rowsAffected = jdbcTemplate.update(updateQuery, 
                newNgayDi, 
                newFlightId, 
                employeeId, 
                current.get("NgayDi"), 
                current.get("MaChuyenBay"));

            if (rowsAffected == 0) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Không thể cập nhật phân công");
                return ResponseEntity.badRequest().body(response);
            }

            Map<String, String> response = new HashMap<>();
            response.put("success", "Cập nhật phân công thành công");
            return ResponseEntity.ok(response);

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
            String query = "DELETE FROM PhanCong WHERE MaNV = ? AND MaChuyenBay = ? AND NgayDi = ?";
            jdbcTemplate.update(query, employeeId, flightId, departureDate);

            Map<String, String> response = new HashMap<>();
            response.put("success", "Xóa phân công thành công");
            return ResponseEntity.ok(response);

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
            String query = "SELECT GioDi, GioDen, TenSanBayDi, TenSanBayDen FROM ChuyenBay WHERE MaChuyenBay = ?";
            Map<String, Object> result = jdbcTemplate.queryForMap(query, flightId);

            if (result != null) {
                Map<String, String> response = new HashMap<>();
                response.put("departure_time", ((LocalDateTime)result.get("GioDi")).toString());
                response.put("arrival_time", ((LocalDateTime)result.get("GioDen")).toString());
                response.put("departure_airport", (String)result.get("TenSanBayDi"));
                response.put("arrival_airport", (String)result.get("TenSanBayDen"));
                return ResponseEntity.ok(response);
            }

            Map<String, String> response = new HashMap<>();
            response.put("error", "Không tìm thấy chuyến bay");
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Lỗi khi lấy thông tin chuyến bay: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
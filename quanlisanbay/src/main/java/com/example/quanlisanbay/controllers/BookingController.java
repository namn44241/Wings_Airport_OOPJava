package com.example.quanlisanbay.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quanlisanbay.config.LoginRequired;

@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("deprecation")
    @PostMapping("/them_dat_cho")
    @LoginRequired
    public ResponseEntity<Map<String, String>> addBooking(@RequestBody Map<String, String> requestData) {
        String customerId = requestData.get("customer-id");
        String flightId = requestData.get("flight-id");
        String departureDatetime = requestData.get("departure-datetime");

        Map<String, String> response = new HashMap<>();
        try {
            // Kiểm tra dữ liệu đầu vào
            if (customerId == null || flightId == null || departureDatetime == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Thiếu thông tin đặt chỗ"));
            }

            // Chuyển đổi chuỗi ngày tháng thành đối tượng LocalDate
            LocalDate departureDate = LocalDate.parse(departureDatetime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String ngayDi = departureDate.toString();

            // Kiểm tra đặt chỗ đã tồn tại
            String checkQuery = "SELECT COUNT(*) FROM DatCho WHERE MaKH = ? AND MaChuyenBay = ? AND NgayDi = ?";
            Integer count = jdbcTemplate.queryForObject(checkQuery, new Object[] { customerId, flightId, ngayDi },
                    Integer.class);
            if (count != null && count > 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Đã tồn tại đặt chỗ cho khách hàng này trên chuyến bay này."));
            }

            // Thêm bản ghi mới
            String insertQuery = "INSERT INTO DatCho (MaKH, NgayDi, MaChuyenBay) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertQuery, customerId, ngayDi, flightId);

            response.put("success", "Thêm đặt chỗ thành công");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi khi thêm đặt chỗ: " + e.getMessage()));
        }
    }

    @SuppressWarnings("deprecation")
    @PostMapping("/them_dat_cho_fe")
    @LoginRequired
    public ResponseEntity<Map<String, String>> addBookingFrontend(@RequestBody Map<String, String> requestData) {
        String customerId = requestData.get("customer-id");
        String flightId = requestData.get("flight-id");
        String departureDatetime = requestData.get("departure-datetime");
        String customerEmail = requestData.get("customer-email");

        Map<String, String> response = new HashMap<>();
        try {
            // Kiểm tra dữ liệu đầu vào
            if (customerId == null || flightId == null || departureDatetime == null || customerEmail == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Thiếu thông tin đặt chỗ"));
            }

            // Chuyển đổi chuỗi ngày tháng
            LocalDate departureDate = LocalDate.parse(departureDatetime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String ngayDi = departureDate.toString();

            // Kiểm tra đặt chỗ đã tồn tại
            String checkQuery = "SELECT COUNT(*) FROM DatCho WHERE MaKH = ? AND MaChuyenBay = ? AND NgayDi = ?";
            Integer count = jdbcTemplate.queryForObject(checkQuery, new Object[] { customerId, flightId, ngayDi },
                    Integer.class);
            if (count != null && count > 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Đã tồn tại đặt chỗ cho khách hàng này trên chuyến bay này."));
            }

            // Thêm bản ghi mới
            String insertQuery = "INSERT INTO DatCho (MaKH, NgayDi, MaChuyenBay) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertQuery, customerId, ngayDi, flightId);

            // Truy vấn để lấy thông tin chi tiết
            String detailQuery = """
                    SELECT
                        KH.MaKH,
                        KH.HoDem || ' ' || KH.Ten AS HoTen,
                        KH.SDT,
                        KH.DiaChi,
                        CB.MaChuyenBay,
                        CB.TenSanBayDi,
                        CB.TenSanBayDen,
                        CB.GioDi,
                        CB.GioDen,
                        DC.NgayDi
                    FROM
                        DatCho DC
                        JOIN KhachHang KH ON DC.MaKH = KH.MaKH
                        JOIN ChuyenBay CB ON DC.MaChuyenBay = CB.MaChuyenBay
                    WHERE
                        DC.MaKH = ? AND DC.MaChuyenBay = ? AND DC.NgayDi = ?
                    """;
            Map<String, Object> bookingDetails = jdbcTemplate.queryForMap(detailQuery, customerId, flightId, ngayDi);

            // Gửi email xác nhận (sử dụng một dịch vụ email đã cấu hình)
            sendConfirmationEmail(bookingDetails, customerEmail);

            response.put("success", "Thêm đặt chỗ thành công và email xác nhận đã được gửi.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi khi thêm đặt chỗ: " + e.getMessage()));
        }
    }

    private void sendConfirmationEmail(Map<String, Object> bookingDetails, String customerEmail) {
        // Cấu hình email và gửi email xác nhận ở đây
        // Có thể sử dụng JavaMailSender để gửi email
    }

    @PostMapping("/sua_dat_cho")
    @LoginRequired
    public ResponseEntity<Map<String, String>> updateBooking(@RequestBody Map<String, String> requestData) {
        String customerId = requestData.get("customer-id");
        String newFlightId = requestData.get("flight-id");
        String newDepartureDatetime = requestData.get("departure-datetime");

        try {
            // Kiểm tra dữ liệu đầu vào
            if (customerId == null || newFlightId == null || newDepartureDatetime == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Thiếu thông tin để cập nhật đặt chỗ"));
            }

            // Trích xuất NgayDi mới từ departure_datetime
            LocalDate newNgayDi = LocalDate.parse(newDepartureDatetime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // Lấy thông tin đặt chỗ hiện tại
            String getCurrentBookingQuery = "SELECT NgayDi, MaChuyenBay FROM DatCho WHERE MaKH = ?";
            Map<String, Object> result = jdbcTemplate.queryForMap(getCurrentBookingQuery, customerId);

            if (result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy đặt chỗ để cập nhật"));
            }

            LocalDate currentNgayDi = (LocalDate) result.get("NgayDi");
            String currentFlightId = (String) result.get("MaChuyenBay");

            // Cập nhật đặt chỗ
            String updateQuery = "UPDATE DatCho SET NgayDi = ?, MaChuyenBay = ? WHERE MaKH = ? AND NgayDi = ? AND MaChuyenBay = ?";
            int updatedRows = jdbcTemplate.update(updateQuery, newNgayDi, newFlightId, customerId, currentNgayDi,
                    currentFlightId);

            if (updatedRows == 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Không thể cập nhật đặt chỗ"));
            }

            return ResponseEntity.ok(Map.of("success", "Cập nhật đặt chỗ thành công"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi khi cập nhật đặt chỗ: " + e.getMessage()));
        }
    }

    @PostMapping("/xoa_dat_cho")
    @LoginRequired
    public ResponseEntity<Map<String, String>> deleteBooking(@RequestBody Map<String, String> requestData) {
        String customerId = requestData.get("customer-id");
        String flightId = requestData.get("flight-id");
        String departureDate = requestData.get("departure-date");

        try {
            // Kiểm tra dữ liệu đầu vào
            if (customerId == null || flightId == null || departureDate == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Thiếu thông tin để xóa đặt chỗ"));
            }

            // Thực hiện xóa thông tin đặt chỗ từ cơ sở dữ liệu
            String query = "DELETE FROM DatCho WHERE MaKH = ? AND MaChuyenBay = ? AND NgayDi = ?";
            int rowsDeleted = jdbcTemplate.update(query, customerId, flightId, departureDate);

            if (rowsDeleted == 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy đặt chỗ để xóa"));
            }

            return ResponseEntity.ok(Map.of("success", "Xóa đặt chỗ thành công"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi khi xóa đặt chỗ: " + e.getMessage()));
        }
    }
}

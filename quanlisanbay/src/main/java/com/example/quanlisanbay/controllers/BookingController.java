package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import java.util.stream.Stream;  
import org.slf4j.Logger;         
import org.slf4j.LoggerFactory;   


@Controller
public class BookingController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private JavaMailSender mailSender;

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    @PostMapping("/them_dat_cho")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themDatCho(
            @RequestParam("customer-id") String customerId,
            @RequestParam("flight-id") String flightId,
            @RequestParam("departure-datetime") String departureDatetime
    ) {
        try {
            LocalDateTime departureDate = LocalDateTime.parse(departureDatetime);
            String ngayDi = departureDate.toLocalDate().toString();

            // Kiểm tra đặt chỗ tồn tại
            String checkQuery = "SELECT COUNT(*) FROM DatCho WHERE MaKH = ? AND MaChuyenBay = ? AND NgayDi = ?";
            int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, customerId, flightId, ngayDi);

            if (count > 0) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Đã tồn tại đặt chỗ cho khách hàng này trên chuyến bay này.");
                return ResponseEntity.badRequest().body(response);
            }

            // Thêm đặt chỗ mới
            String insertQuery = "INSERT INTO DatCho (MaKH, NgayDi, MaChuyenBay) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertQuery, customerId, ngayDi, flightId);

            Map<String, String> response = new HashMap<>();
            response.put("success", "Thêm đặt chỗ thành công");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Lỗi khi thêm đặt chỗ: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/them_dat_cho_fe")
    @ResponseBody
    public ResponseEntity<?> themDatChoFe(
            @RequestParam(value = "customer-id") String customerId,
            @RequestParam(value = "flight-id") String flightId,
            @RequestParam(value = "departure-datetime") String departureDatetime,
            @RequestParam(value = "customer-email") String customerEmail
    ) {
        try {
            // Validate input
            if (Stream.of(customerId, flightId, departureDatetime, customerEmail).anyMatch(String::isEmpty)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Thiếu thông tin đặt chỗ"));
            }

            LocalDateTime departureDate = LocalDateTime.parse(departureDatetime);
            String ngayDi = departureDate.toLocalDate().toString();

            // Kiểm tra đặt chỗ tồn tại
            String checkQuery = "SELECT COUNT(*) FROM DatCho WHERE MaKH = ? AND MaChuyenBay = ? AND NgayDi = ?";
            int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, customerId, flightId, ngayDi);

            if (count > 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Đã tồn tại đặt chỗ cho khách hàng này"));
            }

            // Thêm đặt chỗ mới
            String insertQuery = "INSERT INTO DatCho (MaKH, NgayDi, MaChuyenBay) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertQuery, customerId, ngayDi, flightId);

            // Lấy chi tiết đặt chỗ
            String detailQuery = """
                SELECT 
                    KH.MaKH, 
                    CONCAT(KH.HoDem, ' ', KH.Ten) AS HoTen,
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

            // Gửi email
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                
                helper.setFrom("wingsairport73@gmail.com");
                helper.setTo(customerEmail);
                helper.setSubject("Xác nhận đặt chỗ - Wings Airport");
                
                String htmlBody = String.format("""
                    <html>
                        <body>
                            <h1>Xin chào, %s</h1>
                            <h3>Cảm ơn bạn đã đặt chỗ tại Wings Airport, chúc bạn một ngày tốt lành!</h3>
                            <img width="480" height="269" src="https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExY281dmpqY3h2aXRobDNvaWwzbW1hNWozY281b3l6cWZpMHhpMmoxbCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/n5H1abNYFl6F3KttyW/giphy-downsized-large.gif" alt="dat-cho">
                            <p>Chi tiết đặt chỗ của bạn:</p>
                            <ul>
                                <li>Mã khách hàng: %s</li>
                                <li>Số điện thoại: %s</li>
                                <li>Địa chỉ: %s</li>
                                <li>Mã chuyến bay: %s</li>
                                <li>Ngày đi: %s</li>
                                <li>Sân bay đi: %s</li>
                                <li>Sân bay đến: %s</li>
                                <li>Giờ đi: %s</li>
                                <li>Giờ đến: %s</li>
                                <li>Nhân viên phục vụ: Đang cập nhật..</li>
                            </ul>
                            <p>Wings Airport tự hào là Sân bay hàng không quốc tế 4 sao.<br>
                            Xin trân trọng cảm ơn sự đồng hành của Quý khách và bạn hàng!</p>
                            <p>Chúc bạn có một chuyến bay tuyệt vời!</p>
                            <p>Trân trọng,<br>Đội ngũ Wings Airport</p>
                        </body>
                    </html>
                """, 
                    bookingDetails.get("HoTen"),
                    bookingDetails.get("MaKH"),
                    bookingDetails.get("SDT"),
                    bookingDetails.get("DiaChi"),
                    bookingDetails.get("MaChuyenBay"),
                    bookingDetails.get("NgayDi"),
                    bookingDetails.get("TenSanBayDi"),
                    bookingDetails.get("TenSanBayDen"),
                    bookingDetails.get("GioDi"),
                    bookingDetails.get("GioDen")
                );
                
                helper.setText(htmlBody, true);
                mailSender.send(message);
                
                return ResponseEntity.ok(Map.of("success", "Thêm đặt chỗ thành công và email xác nhận đã được gửi."));
            } catch (Exception emailError) {
                log.error("Error sending email: ", emailError);
                return ResponseEntity.ok(Map.of("success", "Thêm đặt chỗ thành công nhưng không gửi được email xác nhận."));
            }

        } catch (Exception e) {
            log.error("Error in booking process: ", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi không xác định: " + e.getMessage()));
        }
    }

    @PostMapping("/sua_dat_cho")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaDatCho(
            @RequestParam("customer-id") String customerId,
            @RequestParam("flight-id") String newFlightId,
            @RequestParam("departure-datetime") String newDepartureDatetime
    ) {
        try {
            LocalDateTime newNgayDi = LocalDateTime.parse(newDepartureDatetime).toLocalDate().atStartOfDay();

            // Lấy thông tin đặt chỗ hiện tại
            String getCurrentBookingQuery = "SELECT NgayDi, MaChuyenBay FROM DatCho WHERE MaKH = ?";
            Map<String, Object> current = jdbcTemplate.queryForMap(getCurrentBookingQuery, customerId);

            if (current == null) {
                return ResponseEntity.notFound().build();
            }

            // Cập nhật đặt chỗ
            String updateQuery = """
                UPDATE DatCho 
                SET NgayDi = ?, MaChuyenBay = ?
                WHERE MaKH = ? AND NgayDi = ? AND MaChuyenBay = ?
            """;
            int rowsAffected = jdbcTemplate.update(updateQuery, 
                newNgayDi, 
                newFlightId, 
                customerId, 
                current.get("NgayDi"), 
                current.get("MaChuyenBay")
            );

            if (rowsAffected == 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Không thể cập nhật đặt chỗ"));
            }

            return ResponseEntity.ok(Map.of("success", "Cập nhật đặt chỗ thành công"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi khi cập nhật đặt chỗ: " + e.getMessage()));
        }
    }

    @PostMapping("/xoa_dat_cho")
    @LoginRequired
    public String xoaDatCho(
            @RequestParam("customer_id") String customerId,
            @RequestParam("flight_id") String flightId,
            @RequestParam("departure_date") String departureDate,
            RedirectAttributes redirectAttributes
    ) {
        try {
            String query = "DELETE FROM DatCho WHERE MaKH = ? AND MaChuyenBay = ? AND NgayDi = ?";
            jdbcTemplate.update(query, customerId, flightId, departureDate);
            redirectAttributes.addFlashAttribute("success", "Xóa đặt chỗ thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa đặt chỗ: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
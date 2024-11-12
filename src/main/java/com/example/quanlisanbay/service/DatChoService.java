package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.DatCho;
import com.example.quanlisanbay.repository.DatChoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional
public class DatChoService {
    @Autowired
    private DatChoRepository datChoRepository;
    
    @Autowired
    private JavaMailSender mailSender;
    
    private static final Logger log = LoggerFactory.getLogger(DatChoService.class);

    public ResponseEntity<?> themDatCho(String maKH, String maChuyenBay, LocalDate ngayDi) {
        try {
            if (datChoRepository.existsByMaKHAndMaChuyenBayAndNgayDi(maKH, maChuyenBay, ngayDi)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Đã tồn tại đặt chỗ cho khách hàng này trên chuyến bay này."));
            }
            
            DatCho datCho = new DatCho(maKH, maChuyenBay, ngayDi);
            datChoRepository.save(datCho);
            
            return ResponseEntity.ok(Map.of("success", "Thêm đặt chỗ thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi thêm đặt chỗ: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> themDatChoFe(String maKH, String maChuyenBay, LocalDate ngayDi, String customerEmail) {
        try {
            // Validate input
            if (Stream.of(maKH, maChuyenBay, customerEmail).anyMatch(String::isEmpty)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Thiếu thông tin đặt chỗ"));
            }

            if (datChoRepository.existsByMaKHAndMaChuyenBayAndNgayDi(maKH, maChuyenBay, ngayDi)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Đã tồn tại đặt chỗ cho khách hàng này"));
            }

            // Thêm đặt chỗ mới
            DatCho datCho = new DatCho(maKH, maChuyenBay, ngayDi);
            datChoRepository.save(datCho);

            // Lấy chi tiết đặt chỗ
            Map<String, Object> bookingDetails = datChoRepository.findBookingDetails(maKH, maChuyenBay, ngayDi);

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
            return ResponseEntity.internalServerError()
                .body(Map.of("success", "Đã thêm đặt chỗ thành công"));
        }
    }

    public ResponseEntity<?> suaDatCho(String maKH, String newMaChuyenBay, LocalDate newNgayDi) {
        try {
            // Kiểm tra số lượng đặt chỗ hiện tại của khách hàng
            int bookingCount = datChoRepository.countByMaKH(maKH);

            if (bookingCount > 1) {
                return ResponseEntity.badRequest().body(
                    Map.of("error", "Khách hàng này có nhiều hơn 1 đặt chỗ. Không thể sửa thông tin.")
                );
            }

            Optional<DatCho> currentBooking = datChoRepository.findById(maKH);
            if (currentBooking.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            DatCho datCho = currentBooking.get();
            datCho.setMaChuyenBay(newMaChuyenBay);
            datCho.setNgayDi(newNgayDi);
            datChoRepository.save(datCho);

            return ResponseEntity.ok(Map.of("success", "Cập nhật đặt chỗ thành công"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi cập nhật đặt chỗ: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> xoaDatCho(String maKH, String maChuyenBay, LocalDate ngayDi) {
        try {
            DatCho datCho = new DatCho(maKH, maChuyenBay, ngayDi);
            datChoRepository.delete(datCho);
            return ResponseEntity.ok(Map.of("success", "Xóa đặt chỗ thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi xóa đặt chỗ: " + e.getMessage()));
        }
    }
} 
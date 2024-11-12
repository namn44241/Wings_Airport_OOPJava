package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.LichBay;
import com.example.quanlisanbay.repository.LichBayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
public class LichBayService {
    @Autowired
    private LichBayRepository lichBayRepository;

    public ResponseEntity<?> themLich(String maChuyenBay, String soHieu) {
        try {
            if (lichBayRepository.existsByMaChuyenBay(maChuyenBay)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Mã chuyến bay này đã có lịch bay!"));
            }

            String maLoai = lichBayRepository.findMaLoaiByMayBay(soHieu);
            if (maLoai == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Không tìm thấy MaLoai cho máy bay này"));
            }

            // Lấy ngày đi từ ChuyenBay và tạo LichBay mới
            LocalDateTime ngayDi = LocalDateTime.now(); // Cần thay bằng logic lấy từ ChuyenBay
            LichBay lichBay = new LichBay(maChuyenBay, ngayDi.toLocalDate(), soHieu, maLoai);
            lichBayRepository.save(lichBay);

            return ResponseEntity.ok(Map.of("success", "Thêm lịch bay thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi thêm lịch bay: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> suaLich(String maChuyenBay, String soHieu) {
        try {
            if (!lichBayRepository.existsByMaChuyenBay(maChuyenBay)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Lịch bay không tồn tại"));
            }

            LichBay lichBay = lichBayRepository.findById(maChuyenBay).get();
            lichBay.setSoHieu(soHieu);
            lichBayRepository.save(lichBay);

            return ResponseEntity.ok(Map.of("success", "Cập nhật lịch bay thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi cập nhật lịch bay: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> xoaLich(String maChuyenBay, String soHieu) {
        try {
            int datChoCount = lichBayRepository.countDatChoByMaChuyenBay(maChuyenBay);
            int phanCongCount = lichBayRepository.countPhanCongByMaChuyenBay(maChuyenBay);

            if (datChoCount > 0 || phanCongCount > 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Không thể xóa. Mã chuyến bay đã được sử dụng trong bảng DatCho hoặc PhanCong."));
            }

            LichBay lichBay = lichBayRepository.findById(maChuyenBay).get();
            if (lichBay.getSoHieu().equals(soHieu)) {
                lichBayRepository.delete(lichBay);
                return ResponseEntity.ok(Map.of("success", "Xóa lịch bay thành công"));
            }

            return ResponseEntity.badRequest()
                .body(Map.of("error", "Không tìm thấy lịch bay phù hợp"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi xóa lịch bay: " + e.getMessage()));
        }
    }
} 
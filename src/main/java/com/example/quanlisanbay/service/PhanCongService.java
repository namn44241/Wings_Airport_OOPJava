package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.PhanCong;
import com.example.quanlisanbay.repository.PhanCongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class PhanCongService {
    @Autowired
    private PhanCongRepository phanCongRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public ResponseEntity<?> themPhanCong(String maNV, String maChuyenBay, LocalDate ngayDi) {
        try {
            if (phanCongRepository.existsByMaNVAndMaChuyenBayAndNgayDi(maNV, maChuyenBay, ngayDi)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Đã tồn tại phân công cho nhân viên này trên chuyến bay này."));
            }
            
            PhanCong phanCong = new PhanCong(maNV, maChuyenBay, ngayDi);
            phanCongRepository.save(phanCong);
            
            return ResponseEntity.ok(Map.of("success", "Thêm phân công thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi thêm phân công: " + e.getMessage()));
        }
    }
    
    public ResponseEntity<?> suaPhanCong(String maNV, String newMaChuyenBay, LocalDate newNgayDi) {
        try {
            Optional<PhanCong> currentPhanCong = phanCongRepository.findByMaNV(maNV);
            
            if (currentPhanCong.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            PhanCong current = currentPhanCong.get();
            int rowsAffected = phanCongRepository.updatePhanCong(
                maNV, newNgayDi, newMaChuyenBay, 
                current.getNgayDi(), current.getMaChuyenBay()
            );
            
            if (rowsAffected == 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Không thể cập nhật phân công"));
            }
            
            return ResponseEntity.ok(Map.of("success", "Cập nhật phân công thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi cập nhật phân công: " + e.getMessage()));
        }
    }
    
    public ResponseEntity<?> xoaPhanCong(String maNV, String maChuyenBay, LocalDate ngayDi) {
        try {
            phanCongRepository.deleteByMaNVAndMaChuyenBayAndNgayDi(maNV, maChuyenBay, ngayDi);
            return ResponseEntity.ok(Map.of("success", "Xóa phân công thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi xóa phân công: " + e.getMessage()));
        }
    }
    
    public ResponseEntity<?> getFlightDetailsForAssignment(String flightId) {
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

            return ResponseEntity.notFound()
                .build();

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Lỗi khi lấy thông tin chuyến bay: " + e.getMessage()));
        }
    }
} 
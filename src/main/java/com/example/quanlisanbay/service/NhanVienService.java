package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.NhanVien;
import com.example.quanlisanbay.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class NhanVienService {
    @Autowired
    private NhanVienRepository nhanVienRepository;
    
    private static final List<String> VALID_EMPLOYEE_TYPES = Arrays.asList("Tiếp viên", "Phi công");

    public ResponseEntity<?> themNhanVien(String maNV, String hoDem, String ten, 
            String sdt, String diaChi, String luong, String loaiNV) {
        try {
            if (!VALID_EMPLOYEE_TYPES.contains(loaiNV)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Loại nhân viên không hợp lệ"));
            }

            BigDecimal luongDecimal = new BigDecimal(luong);
            NhanVien nhanVien = new NhanVien(maNV, hoDem, ten, sdt, diaChi, luongDecimal, loaiNV);
            nhanVienRepository.save(nhanVien);
            
            return ResponseEntity.ok(Map.of("status", "success", "message", "Thêm nhân viên thành công"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("status", "error", "message", "Lương phải là một số hợp lệ"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("status", "error", "message", "Lỗi khi thêm nhân viên: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> suaNhanVien(String maNV, String hoDem, String ten, 
            String sdt, String diaChi, String luong, String loaiNV) {
        try {
            if (!VALID_EMPLOYEE_TYPES.contains(loaiNV)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Loại nhân viên không hợp lệ"));
            }

            Optional<NhanVien> nhanVienOpt = nhanVienRepository.findById(maNV);
            if (nhanVienOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Không tìm thấy nhân viên"));
            }

            BigDecimal luongDecimal = new BigDecimal(luong);
            NhanVien nhanVien = nhanVienOpt.get();
            nhanVien.setHoDem(hoDem);
            nhanVien.setTen(ten);
            nhanVien.setSdt(sdt);
            nhanVien.setDiaChi(diaChi);
            nhanVien.setLuong(luongDecimal);
            nhanVien.setLoaiNV(loaiNV);
            
            nhanVienRepository.save(nhanVien);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Cập nhật thông tin nhân viên thành công"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("status", "error", "message", "Lương phải là một số hợp lệ"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("status", "error", "message", "Lỗi khi cập nhật thông tin nhân viên: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> xoaNhanVien(String maNV) {
        try {
            int count = nhanVienRepository.countPhanCongByMaNV(maNV);
            if (count > 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Không thể xóa nhân viên này vì đã được phân công."));
            }

            nhanVienRepository.deleteById(maNV);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Đã xóa nhân viên thành công."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("status", "error", "message", "Có lỗi xảy ra: " + e.getMessage()));
        }
    }
} 
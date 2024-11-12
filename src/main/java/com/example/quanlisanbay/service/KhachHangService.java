package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.KhachHang;
import com.example.quanlisanbay.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class KhachHangService {
    @Autowired
    private KhachHangRepository khachHangRepository;

    public ResponseEntity<?> themKhachHang(String maKH, String sdt, String hoDem, String ten, String diaChi) {
        try {
            KhachHang khachHang = new KhachHang(maKH, sdt, hoDem, ten, diaChi);
            khachHangRepository.save(khachHang);
            return ResponseEntity.ok(Map.of("success", true, "message", "Khách hàng đã được thêm thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    public String getNextCustomerId() {
        String maxMaKH = khachHangRepository.findMaxMaKH();
        if (maxMaKH != null) {
            int maxIdNum = Integer.parseInt(maxMaKH.substring(2));
            return String.format("KH%06d", maxIdNum + 1);
        }
        return "KH000001";
    }

    public ResponseEntity<?> suaKhachHang(String maKH, String sdt, String hoDem, String ten, String diaChi) {
        try {
            Optional<KhachHang> khachHangOpt = khachHangRepository.findById(maKH);
            if (khachHangOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Không tìm thấy khách hàng"));
            }

            KhachHang khachHang = khachHangOpt.get();
            khachHang.setSdt(sdt);
            khachHang.setHoDem(hoDem);
            khachHang.setTen(ten);
            khachHang.setDiaChi(diaChi);
            khachHangRepository.save(khachHang);

            return ResponseEntity.ok(Map.of("status", "success", "message", "Cập nhật thông tin khách hàng thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("status", "error", "message", "Lỗi khi cập nhật thông tin khách hàng: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> xoaKhachHang(String maKH) {
        try {
            if (khachHangRepository.existsById(maKH)) {
                khachHangRepository.deleteById(maKH);
                return ResponseEntity.ok(Map.of("status", "success", "message", "Đã xóa khách hàng thành công."));
            }
            return ResponseEntity.badRequest()
                .body(Map.of("status", "error", "message", "Không tìm thấy khách hàng"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("status", "error", "message", "Có lỗi xảy ra: " + e.getMessage()));
        }
    }
} 
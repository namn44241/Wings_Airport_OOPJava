package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.LoaiMayBay;
import com.example.quanlisanbay.repository.LoaiMayBayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class LoaiMayBayService {
    @Autowired
    private LoaiMayBayRepository loaiMayBayRepository;

    public ResponseEntity<?> themLoaiMayBay(String maLoai, String hangSanXuat) {
        try {
            LoaiMayBay loaiMayBay = new LoaiMayBay(maLoai, hangSanXuat);
            loaiMayBayRepository.save(loaiMayBay);
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Thêm loại máy bay thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of(
                    "status", "error",
                    "message", "Lỗi khi thêm loại máy bay: " + e.getMessage()
                ));
        }
    }

    public ResponseEntity<?> suaLoaiMayBay(String maLoai, String hangSanXuat) {
        try {
            if (!loaiMayBayRepository.existsById(maLoai)) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "status", "error",
                        "message", "Không tìm thấy loại máy bay"
                    ));
            }

            LoaiMayBay loaiMayBay = loaiMayBayRepository.findById(maLoai).get();
            loaiMayBay.setHangSanXuat(hangSanXuat);
            loaiMayBayRepository.save(loaiMayBay);
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Cập nhật thông tin loại máy bay thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of(
                    "status", "error",
                    "message", "Lỗi khi cập nhật thông tin loại máy bay: " + e.getMessage()
                ));
        }
    }

    public ResponseEntity<?> xoaLoaiMayBay(String maLoai) {
        try {
            int count = loaiMayBayRepository.countUsedTypes(maLoai);
            
            if (count > 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "status", "error",
                        "message", "Loại máy bay này đã được sử dụng trong bảng Máy bay hoặc Lịch bay"
                    ));
            }

            loaiMayBayRepository.deleteById(maLoai);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Đã xóa loại máy bay thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of(
                    "status", "error",
                    "message", "Có lỗi xảy ra: " + e.getMessage()
                ));
        }
    }
} 
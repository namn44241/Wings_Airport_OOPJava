package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.MayBay;
import com.example.quanlisanbay.repository.MayBayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class MayBayService {
    @Autowired
    private MayBayRepository mayBayRepository;

    public ResponseEntity<?> themMayBay(String soHieu, String maLoai, String soGheNgoi) {
        try {
            if (soHieu.length() > 10) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, 
                                "message", "Số hiệu máy bay không được vượt quá 10 ký tự"));
            }

            if (mayBayRepository.existsBySoHieu(soHieu)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, 
                                "message", "Số hiệu máy bay đã tồn tại trong hệ thống"));
            }

            if (mayBayRepository.countLoaiMayBayByMaLoai(maLoai) == 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, 
                                "message", "Loại máy bay không tồn tại!"));
            }

            try {
                int soGhe = Integer.parseInt(soGheNgoi);
                if (soGhe <= 0) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("success", false,
                                    "message", "Số ghế ngồi phải lớn hơn 0"));
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false,
                                "message", "Số ghế ngồi không hợp lệ"));
            }

            MayBay mayBay = new MayBay(soHieu, maLoai, Integer.parseInt(soGheNgoi));
            mayBayRepository.save(mayBay);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("success", true, 
                            "message", "Thêm máy bay thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, 
                            "message", "Lỗi khi thêm máy bay: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> suaMayBay(String soHieu, String maLoai, String soGheNgoi) {
        try {
            if (!mayBayRepository.existsById(soHieu)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, 
                                "message", "Máy bay không tồn tại!"));
            }

            if (mayBayRepository.countLichBayBySoHieu(soHieu) > 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, 
                                "message", "Không thể sửa thông tin máy bay này vì đã được sử dụng trong lịch bay!"));
            }

            MayBay mayBay = mayBayRepository.findById(soHieu).get();
            mayBay.setMaLoai(maLoai);
            mayBay.setSoGheNgoi(Integer.parseInt(soGheNgoi));
            mayBayRepository.save(mayBay);

            return ResponseEntity.ok(Map.of("success", true, 
                                          "message", "Cập nhật thông tin máy bay thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, 
                            "message", "Lỗi khi cập nhật máy bay: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> xoaMayBay(String soHieu) {
        try {
            int scheduleCount = mayBayRepository.countLichBayBySoHieu(soHieu);
            int typeCount = mayBayRepository.countLoaiMayBayUsedBySoHieu(soHieu);

            if (scheduleCount > 0 || typeCount > 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, 
                                "message", "Máy bay đang được sử dụng trong bảng Lịch bay hoặc Loại máy bay"));
            }

            mayBayRepository.deleteById(soHieu);
            return ResponseEntity.ok(Map.of("success", true, 
                                          "message", "Đã xóa máy bay " + soHieu + " thành công."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, 
                            "message", "Lỗi khi xóa máy bay: " + e.getMessage()));
        }
    }
} 
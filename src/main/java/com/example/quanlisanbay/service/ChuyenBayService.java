package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.ChuyenBay;
import com.example.quanlisanbay.repository.ChuyenBayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ChuyenBayService {
    @Autowired
    private ChuyenBayRepository chuyenBayRepository;

    public String themChuyenBay(String maChuyenBay, String tenSanBayDi, String tenSanBayDen,
                               String gioDi, String gioDen, RedirectAttributes redirectAttributes) {
        try {
            if (tenSanBayDi.equals(tenSanBayDen)) {
                redirectAttributes.addFlashAttribute("error", "Lỗi: Sân bay đi và đến phải khác nhau");
                return "redirect:/admin";
            }

            LocalDateTime gioDiDateTime = LocalDateTime.parse(gioDi);
            LocalDateTime gioDenDateTime = LocalDateTime.parse(gioDen);

            ChuyenBay chuyenBay = new ChuyenBay(maChuyenBay, tenSanBayDi, tenSanBayDen, 
                                               gioDiDateTime, gioDenDateTime);
            chuyenBayRepository.save(chuyenBay);
            
            redirectAttributes.addFlashAttribute("success", "Thêm chuyến bay thành công");
            return "redirect:/admin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin";
        }
    }

    public ResponseEntity<?> suaChuyenBay(String maChuyenBay, String tenSanBayDi, 
            String tenSanBayDen, String gioDi, String gioDen) {
        try {
            if (tenSanBayDi.equals(tenSanBayDen)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Lỗi: Sân bay đi và đến phải khác nhau"));
            }

            Optional<ChuyenBay> chuyenBayOpt = chuyenBayRepository.findById(maChuyenBay);
            if (chuyenBayOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Không tìm thấy chuyến bay"));
            }

            LocalDateTime gioDiDateTime = LocalDateTime.parse(gioDi);
            LocalDateTime gioDenDateTime = LocalDateTime.parse(gioDen);

            ChuyenBay chuyenBay = chuyenBayOpt.get();
            chuyenBay.setTenSanBayDi(tenSanBayDi);
            chuyenBay.setTenSanBayDen(tenSanBayDen);
            chuyenBay.setGioDi(gioDiDateTime);
            chuyenBay.setGioDen(gioDenDateTime);
            
            chuyenBayRepository.save(chuyenBay);
            return ResponseEntity.ok(Map.of("status", "success", 
                "message", "Cập nhật thông tin chuyến bay thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("status", "error", 
                    "message", "Lỗi khi cập nhật thông tin chuyến bay: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> xoaChuyenBay(String maChuyenBay) {
        try {
            int count = chuyenBayRepository.countUsedFlights(maChuyenBay);
            if (count > 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", 
                        "message", "Chuyến bay này đã được dùng để đặt Lịch bay, Đặt chỗ hoặc được Phân công nên không thể xóa!"));
            }

            chuyenBayRepository.deleteById(maChuyenBay);
            return ResponseEntity.ok(Map.of("status", "success", 
                "message", "Đã xóa chuyến bay thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("status", "error", "message", "Có lỗi xảy ra: " + e.getMessage()));
        }
    }
} 
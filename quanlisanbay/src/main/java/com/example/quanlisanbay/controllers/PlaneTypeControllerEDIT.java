package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.service.PlaneTypeServiceEDIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PlaneTypeControllerEDIT {

    @Autowired
    private PlaneTypeServiceEDIT planeTypeServiceEDIT;

    // API để cập nhật loại máy bay
    @PostMapping("/sua_loai_mb/{plane_type_id}")
    public ResponseEntity<?> updatePlaneType(@PathVariable("plane_type_id") String planeTypeId,
            @RequestParam("manufacturer") String manufacturer) {
        try {
            // Gọi service để cập nhật dữ liệu
            planeTypeServiceEDIT.updatePlaneType(planeTypeId, manufacturer);
            return ResponseEntity.ok().body("Cập nhật thông tin loại máy bay thành công");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi cập nhật thông tin loại máy bay: " + e.getMessage());
        }
    }
}
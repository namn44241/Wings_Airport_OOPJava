package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.service.PlaneTypeServiceDELETE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaneTypeControllerDELETE {

    @Autowired
    private PlaneTypeServiceDELETE planeTypeService;

    // API xóa loại máy bay
    @DeleteMapping("/xoa_loai_mb/{planeTypeId}")
    public ResponseEntity<String> deletePlaneType(@PathVariable String planeTypeId) {
        try {
            planeTypeService.deletePlaneType(planeTypeId);
            return ResponseEntity.ok("Loại máy bay đã được xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Không thể xóa loại máy bay: " + e.getMessage());
        }
    }
}

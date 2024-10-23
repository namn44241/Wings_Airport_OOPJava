package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.PlaneType;
import com.example.quanlisanbay.repository.PlaneTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PlaneTypeService {

    @Autowired
    private PlaneTypeRepository planeTypeRepository;

    public ResponseEntity<?> addPlaneType(PlaneType planeType) {
        try {
            planeTypeRepository.save(planeType);
            return ResponseEntity.ok().body("Thêm loại máy bay thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi thêm loại máy bay: " + e.getMessage());
        }
    }
}

package com.example.quanlisanbay.service;

import com.example.quanlisanbay.repository.PlaneTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaneTypeServiceDELETE {

    @Autowired
    private PlaneTypeRepository planeTypeRepository;

    // Phương thức xóa loại máy bay
    public void deletePlaneType(String planeTypeId) {
        if (planeTypeRepository.existsById(planeTypeId)) {
            planeTypeRepository.deleteById(planeTypeId);
        } else {
            throw new RuntimeException("Không tìm thấy loại máy bay với ID: " + planeTypeId);
        }
    }
}

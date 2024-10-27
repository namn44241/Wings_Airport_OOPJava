package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.model.PlaneType;
import com.example.quanlisanbay.service.PlaneTypeService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PlaneTypeController {

    @Autowired
    private PlaneTypeService planeTypeService;

    @PostMapping("/them_loai_mb")
    public ResponseEntity<?> addPlaneType(@Valid @RequestBody PlaneType planeType) {
        try {
            return planeTypeService.addPlaneType(planeType);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
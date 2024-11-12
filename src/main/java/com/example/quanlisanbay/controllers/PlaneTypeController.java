package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import com.example.quanlisanbay.service.LoaiMayBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PlaneTypeController {

    @Autowired
    private LoaiMayBayService loaiMayBayService;

    @PostMapping("/them_loai_mb")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themLoaiMayBay(
            @RequestParam("plane-type-id") String planeTypeId,
            @RequestParam("manufacturer") String manufacturer) {
        return loaiMayBayService.themLoaiMayBay(planeTypeId, manufacturer);
    }

    @PostMapping("/sua_loai_mb/{planeTypeId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaLoaiMayBay(
            @PathVariable String planeTypeId,
            @RequestParam("manufacturer") String manufacturer) {
        return loaiMayBayService.suaLoaiMayBay(planeTypeId, manufacturer);
    }

    @PostMapping("/xoa_loai_mb/{planeTypeId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaLoaiMayBay(@PathVariable String planeTypeId) {
        return loaiMayBayService.xoaLoaiMayBay(planeTypeId);
    }
}
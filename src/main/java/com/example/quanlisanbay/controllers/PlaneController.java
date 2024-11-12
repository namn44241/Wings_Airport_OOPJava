package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import com.example.quanlisanbay.service.MayBayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PlaneController {

    @Autowired
    private MayBayService mayBayService;

    @PostMapping("/them_mb")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themMayBay(
            @RequestParam("plane-id") String planeId,
            @RequestParam("plane-type-id") String planeTypeId,
            @RequestParam("seat-quantity") String seatQuantity) {
        return mayBayService.themMayBay(planeId, planeTypeId, seatQuantity);
    }

    @PostMapping("/sua_mb")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaMayBay(
            @RequestParam("plane-id") String planeId,
            @RequestParam("plane-type-id") String planeTypeId,
            @RequestParam("seat-quantity") String seatQuantity) {
        return mayBayService.suaMayBay(planeId, planeTypeId, seatQuantity);
    }

    @PostMapping("/xoa_mb/{planeId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaMayBay(@PathVariable String planeId) {
        return mayBayService.xoaMayBay(planeId);
    }
}
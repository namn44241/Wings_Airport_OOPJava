package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import com.example.quanlisanbay.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class CustomerController {

    @Autowired
    private KhachHangService khachHangService;

    @PostMapping("/them_kh")
    @LoginRequired
    public String themKhachHang(
            @RequestParam("customer-id") String customerId,
            @RequestParam("customer-phone") String customerPhone,
            @RequestParam("customer-last-name") String customerLastName,
            @RequestParam("customer-first-name") String customerFirstName,
            @RequestParam("customer-address") String customerAddress) {
        khachHangService.themKhachHang(customerId, customerPhone, customerLastName, customerFirstName, customerAddress);
        return "redirect:/admin";
    }

    @GetMapping("/next_customer_id")
    @ResponseBody
    public ResponseEntity<?> getNextCustomerId() {
        String nextCustomerId = khachHangService.getNextCustomerId();
        return ResponseEntity.ok(Map.of("next_customer_id", nextCustomerId));
    }

    @PostMapping("/them_kh_fe")
    @ResponseBody
    public ResponseEntity<?> themKhachHangFe(
            @RequestParam("customer-id") String customerId,
            @RequestParam("customer-phone") String customerPhone,
            @RequestParam("customer-last-name") String customerLastName,
            @RequestParam("customer-first-name") String customerFirstName,
            @RequestParam("customer-address") String customerAddress) {
        return khachHangService.themKhachHang(customerId, customerPhone, customerLastName, customerFirstName, customerAddress);
    }

    @PostMapping("/sua_kh")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaKhachHang(
            @RequestParam("customer-id") String customerId,
            @RequestParam("customer-phone") String customerPhone,
            @RequestParam("customer-last-name") String customerLastName,
            @RequestParam("customer-first-name") String customerFirstName,
            @RequestParam("customer-address") String customerAddress) {
        return khachHangService.suaKhachHang(customerId, customerPhone, customerLastName, customerFirstName, customerAddress);
    }

    @PostMapping("/xoa_kh/{customerId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaKhachHang(@PathVariable String customerId) {
        return khachHangService.xoaKhachHang(customerId);
    }
}

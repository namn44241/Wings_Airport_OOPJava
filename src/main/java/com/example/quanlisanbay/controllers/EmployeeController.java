package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import com.example.quanlisanbay.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeeController {

    @Autowired
    private NhanVienService nhanVienService;

    @PostMapping("/them_nv")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> themNhanVien(
            @RequestParam("employee-id") String employeeId,
            @RequestParam("employee-last-name") String employeeLastName,
            @RequestParam("employee-first-name") String employeeFirstName,
            @RequestParam("employee-phone") String employeePhone,
            @RequestParam("employee-address") String employeeAddress,
            @RequestParam("employee-salary") String employeeSalary,
            @RequestParam("employee-type") String employeeType) {
        
        return nhanVienService.themNhanVien(employeeId, employeeLastName, employeeFirstName,
                employeePhone, employeeAddress, employeeSalary, employeeType);
    }

    @PostMapping("/sua_nv/{employeeId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> suaNhanVien(
            @PathVariable String employeeId,
            @RequestParam("employee-last-name") String employeeLastName,
            @RequestParam("employee-first-name") String employeeFirstName,
            @RequestParam("employee-phone") String employeePhone,
            @RequestParam("employee-address") String employeeAddress,
            @RequestParam("employee-salary") String employeeSalary,
            @RequestParam("employee-type") String employeeType) {
        
        return nhanVienService.suaNhanVien(employeeId, employeeLastName, employeeFirstName,
                employeePhone, employeeAddress, employeeSalary, employeeType);
    }

    @PostMapping("/xoa_nv/{employeeId}")
    @LoginRequired
    @ResponseBody
    public ResponseEntity<?> xoaNhanVien(@PathVariable String employeeId) {
        return nhanVienService.xoaNhanVien(employeeId);
    }
}
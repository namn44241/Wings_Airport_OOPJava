package com.example.quanlisanbay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DatabaseConnectionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test_connection")
    public String testConnection() {
        try {
            String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES";
            List<String> tables = jdbcTemplate.queryForList(query, String.class);
            return "Kết nối thành công! Danh sách các bảng: " + tables.toString();
        } catch (Exception e) {
            return "Kết nối thất bại: " + e.getMessage();
        }
    }
}

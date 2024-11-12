package com.example.quanlisanbay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SanBayService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getFlights() {
        Map<String, Object> response = new HashMap<>();
        try {
            String query = """
                SELECT 
                    cb.MaChuyenBay, 
                    COALESCE(mb.SoHieu, 'N/A') as SoHieuMayBay,
                    COALESCE(mb.MaLoai, 'N/A') as MaLoaiMayBay,
                    cb.TenSanBayDi,
                    cb.TenSanBayDen,
                    DATE_FORMAT(cb.GioDi, '%Y-%m-%dT%H:%i') as GioDi,
                    DATE_FORMAT(cb.GioDen, '%Y-%m-%dT%H:%i') as GioDen
                FROM ChuyenBay cb
                LEFT JOIN LichBay lb ON cb.MaChuyenBay = lb.MaChuyenBay
                LEFT JOIN MayBay mb ON lb.SoHieu = mb.SoHieu
                ORDER BY cb.GioDi DESC
            """;
            List<Map<String, Object>> flights = jdbcTemplate.queryForList(query);
            response.put("success", true);
            response.put("data", flights);
            
            System.out.println("Response data: " + response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    public ResponseEntity<?> searchFlights(String query) {
        try {
            String searchQuery = """
                SELECT 
                    cb.MaChuyenBay, 
                    COALESCE(mb.SoHieu, 'N/A') as SoHieuMayBay,
                    COALESCE(mb.MaLoai, 'N/A') as MaLoaiMayBay,
                    cb.TenSanBayDi,
                    cb.TenSanBayDen,
                    DATE_FORMAT(cb.GioDi, '%Y-%m-%dT%H:%i') as GioDi,
                    DATE_FORMAT(cb.GioDen, '%Y-%m-%dT%H:%i') as GioDen
                FROM ChuyenBay cb
                LEFT JOIN LichBay lb ON cb.MaChuyenBay = lb.MaChuyenBay 
                LEFT JOIN MayBay mb ON lb.SoHieu = mb.SoHieu
                WHERE 
                    cb.MaChuyenBay LIKE ? OR
                    mb.SoHieu LIKE ? OR 
                    mb.MaLoai LIKE ? OR
                    cb.TenSanBayDi LIKE ? OR
                    cb.TenSanBayDen LIKE ? OR
                    cb.GioDi LIKE ? OR
                    cb.GioDen LIKE ?
                ORDER BY cb.GioDi DESC
            """;

            String searchPattern = "%" + query + "%";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(
                searchQuery, 
                searchPattern, searchPattern, searchPattern, 
                searchPattern, searchPattern, searchPattern, searchPattern
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", results);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 
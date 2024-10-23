package com.example.quanlisanbay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FlightDetailService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResponseEntity<?> getFlightDetails(String flightId) {
        String query = "SELECT GioDi, GioDen FROM ChuyenBay WHERE MaChuyenBay = ?";
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query, flightId);
            if (result != null) {
                return ResponseEntity.ok().body(Map.of(
                        "departure_time", result.get("GioDi").toString(),
                        "arrival_time", result.get("GioDen").toString()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", "Không tìm thấy chuyến bay"));
        }
        return ResponseEntity.status(404).body(Map.of("error", "Không tìm thấy chuyến bay"));
    }
}

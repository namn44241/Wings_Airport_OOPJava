package com.example.quanlisanbay.service;

import com.example.quanlisanbay.repository.SanBayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SanBayService {
    @Autowired
    private SanBayRepository sanBayRepository;

    public Map<String, Object> getFlights() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> flights = sanBayRepository.findAllFlights();
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
            List<Map<String, Object>> results = sanBayRepository.searchFlights(query);
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
package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.Booking;
import com.example.quanlisanbay.model.FlightDTO;
import com.example.quanlisanbay.repository.FlightRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FlightService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired // Khai báo @Autowired để Spring tự động tiêm dependency
    private FlightRepository flightRepository;

    // Lấy tất cả chuyến bay
    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAllFlights();
    }

    // Tìm kiếm chuyến bay
    public List<FlightDTO> searchFlights(String query, String searchType) {
        if ("flight".equalsIgnoreCase(searchType)) {
            return flightRepository.findFlightsByMaChuyenBay(query); // Đảm bảo phương thức này tồn tại trong
                                                                     // FlightRepository
        } else {
            return flightRepository.findFlightsByAirport(query); // Đảm bảo phương thức này tồn tại trong
                                                                 // FlightRepository
        }
    }

    // Lấy thông tin chi tiết chuyến bay
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

    // Đặt chỗ cho chuyến bay
    public boolean bookFlight(Booking booking) {
        try {
            return flightRepository.bookFlight(booking);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNextFlightId() {
        String query = "SELECT MAX(MaChuyenBay) FROM ChuyenBay";
        Integer maxFlightId = jdbcTemplate.queryForObject(query, Integer.class);

        // Kiểm tra xem mã chuyến bay lớn nhất có tồn tại hay không
        if (maxFlightId != null) {
            int nextIdNum = maxFlightId + 1;
            return String.format("%04d", nextIdNum); // Đảm bảo mã chuyến bay có 4 chữ số
        } else {
            return "0001"; // Trả về "0001" nếu không có chuyến bay nào
        }
    }
}

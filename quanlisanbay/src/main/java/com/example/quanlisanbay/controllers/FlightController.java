package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.config.LoginRequired;
import com.example.quanlisanbay.model.Booking;
import com.example.quanlisanbay.model.ErrorResponse;
import com.example.quanlisanbay.model.FlightDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FlightController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(FlightController.class);

    @GetMapping("/flights")
    public ResponseEntity<?> getFlights() {
        try {
            List<FlightDTO> flights = findAllFlights();
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private List<FlightDTO> findAllFlights() {
        String query = """
                SELECT cb.MaChuyenBay, lb.SoHieu AS SoHieuMayBay, lb.MaLoai AS MaLoaiMayBay,
                    cb.TenSanBayDi, cb.TenSanBayDen, cb.GioDi, cb.GioDen, lb.NgayDi
                FROM ChuyenBay cb
                INNER JOIN LichBay lb ON cb.MaChuyenBay = lb.MaChuyenBay
                """;

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            FlightDTO flight = new FlightDTO();
            flight.setMaChuyenBay(rs.getString("MaChuyenBay"));
            flight.setSoHieuMayBay(rs.getString("SoHieuMayBay"));
            flight.setMaLoaiMayBay(rs.getString("MaLoaiMayBay"));
            flight.setTenSanBayDi(rs.getString("TenSanBayDi"));
            flight.setTenSanBayDen(rs.getString("TenSanBayDen"));
            flight.setGioDi(rs.getTimestamp("GioDi"));
            flight.setGioDen(rs.getTimestamp("GioDen"));
            flight.setNgayDi(rs.getDate("NgayDi"));
            return flight;
        });
    }

    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse("An error occurred while fetching flight data", e.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookFlight(@RequestBody Booking booking) {
        logger.info("Received booking: {}", booking);

        if (booking.getCustomerId() == null || booking.getFlightId() == null || booking.getDepartureDate() == null) {
            logger.error("Invalid data: {}", booking);
            return ResponseEntity.status(400).body("{\"success\": false, \"error\": \"Invalid data\"}");
        }

        boolean success = bookFlightInRepo(booking);
        if (success) {
            logger.info("Booking successful: {}", booking);
            return ResponseEntity.ok().body("{\"success\": true}");
        } else {
            logger.error("Booking failed for: {}", booking);
            return ResponseEntity.status(500).body("{\"success\": false, \"error\": \"Booking failed\"}");
        }
    }

    private boolean bookFlightInRepo(Booking booking) {
        String query = "INSERT INTO DatCho (MaKH, MaChuyenBay, NgayDi) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(query, booking.getCustomerId(), booking.getFlightId(), booking.getDepartureDate());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFlights(@RequestParam(name = "query") String query,
            @RequestParam(name = "type") String searchType) {
        try {
            List<FlightDTO> flights;
            if ("flight".equalsIgnoreCase(searchType)) {
                flights = findFlightsByMaChuyenBay(query);
            } else {
                flights = findFlightsByAirport(query);
            }
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while searching flights");
        }
    }

    @SuppressWarnings("deprecation")
    private List<FlightDTO> findFlightsByMaChuyenBay(String maChuyenBay) {
        String query = "SELECT cb.MaChuyenBay, lb.SoHieu AS SoHieuMayBay, lb.MaLoai AS MaLoaiMayBay, " +
                "cb.TenSanBayDi, cb.TenSanBayDen, cb.GioDi, cb.GioDen, lb.NgayDi " +
                "FROM ChuyenBay cb INNER JOIN LichBay lb ON cb.MaChuyenBay = lb.MaChuyenBay " +
                "WHERE cb.MaChuyenBay = ?";

        return jdbcTemplate.query(query, new Object[] { maChuyenBay }, (rs, rowNum) -> {
            FlightDTO flight = new FlightDTO();
            flight.setMaChuyenBay(rs.getString("MaChuyenBay"));
            flight.setSoHieuMayBay(rs.getString("SoHieuMayBay"));
            flight.setMaLoaiMayBay(rs.getString("MaLoaiMayBay"));
            flight.setTenSanBayDi(rs.getString("TenSanBayDi"));
            flight.setTenSanBayDen(rs.getString("TenSanBayDen"));
            flight.setGioDi(rs.getTimestamp("GioDi"));
            flight.setGioDen(rs.getTimestamp("GioDen"));
            flight.setNgayDi(rs.getDate("NgayDi"));
            return flight;
        });
    }

    @SuppressWarnings("deprecation")
    private List<FlightDTO> findFlightsByAirport(String airportName) {
        String query = "SELECT cb.MaChuyenBay, lb.SoHieu AS SoHieuMayBay, lb.MaLoai AS MaLoaiMayBay, " +
                "cb.TenSanBayDi, cb.TenSanBayDen, cb.GioDi, cb.GioDen, lb.NgayDi " +
                "FROM ChuyenBay cb INNER JOIN LichBay lb ON cb.MaChuyenBay = lb.MaChuyenBay " +
                "WHERE cb.TenSanBayDi = ? OR cb.TenSanBayDen = ?";

        return jdbcTemplate.query(query, new Object[] { airportName, airportName }, (rs, rowNum) -> {
            FlightDTO flight = new FlightDTO();
            flight.setMaChuyenBay(rs.getString("MaChuyenBay"));
            flight.setSoHieuMayBay(rs.getString("SoHieuMayBay"));
            flight.setMaLoaiMayBay(rs.getString("MaLoaiMayBay"));
            flight.setTenSanBayDi(rs.getString("TenSanBayDi"));
            flight.setTenSanBayDen(rs.getString("TenSanBayDen"));
            flight.setGioDi(rs.getTimestamp("GioDi"));
            flight.setGioDen(rs.getTimestamp("GioDen"));
            flight.setNgayDi(rs.getDate("NgayDi"));
            return flight;
        });
    }

    @GetMapping("/get_flight_details")
    @LoginRequired
    public ResponseEntity<?> getFlightDetails(@RequestParam("flight_id") String flightId) {
        return fetchFlightDetails(flightId);
    }

    private ResponseEntity<?> fetchFlightDetails(String flightId) {
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

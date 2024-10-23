package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.FlightSearchDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class FlightSearchRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("deprecation")
    public List<FlightSearchDTO> findFlightsByMaChuyenBay(String query) {
        String sql = "SELECT * FROM ChuyenBay WHERE MaChuyenBay LIKE ?";

        // Đảm bảo rằng tên lớp FlightSearchDTO được viết đúng
        return jdbcTemplate.query(sql, new Object[] { "%" + query + "%" }, (rs, rowNum) -> {
            FlightSearchDTO flight = new FlightSearchDTO();
            flight.setMaChuyenBay(rs.getString("MaChuyenBay"));
            flight.setTenSanBayDi(rs.getString("TenSanBayDi"));
            flight.setTenSanBayDen(rs.getString("TenSanBayDen"));

            // Kiểm tra trường hợp nếu GioDi hoặc GioDen có thể null
            flight.setGioDi(rs.getTimestamp("GioDi") != null ? rs.getTimestamp("GioDi") : null);
            flight.setGioDen(rs.getTimestamp("GioDen") != null ? rs.getTimestamp("GioDen") : null);

            return flight;
        });
    }

    @SuppressWarnings("deprecation")
    public List<FlightSearchDTO> findFlightsByAirport(String query) {
        String sql = "SELECT * FROM ChuyenBay WHERE TenSanBayDi LIKE ? OR TenSanBayDen LIKE ?";
        return jdbcTemplate.query(sql, new Object[] { "%" + query + "%", "%" + query + "%" }, (rs, rowNum) -> {
            FlightSearchDTO flight = new FlightSearchDTO();
            flight.setMaChuyenBay(rs.getString("MaChuyenBay"));
            flight.setTenSanBayDi(rs.getString("TenSanBayDi"));
            flight.setTenSanBayDen(rs.getString("TenSanBayDen"));
            flight.setGioDi(rs.getTimestamp("GioDi"));
            flight.setGioDen(rs.getTimestamp("GioDen"));
            return flight;
        });
    }
}

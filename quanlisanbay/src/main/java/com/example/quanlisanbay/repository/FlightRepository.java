package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.Booking;
import com.example.quanlisanbay.model.FlightDTO;
import com.example.quanlisanbay.model.FlightSearchDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class FlightRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Phương thức tìm tất cả chuyến bay
    public List<FlightDTO> findAllFlights() {
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
            flight.setGioDi(rs.getTimestamp("GioDi")); // Đảm bảo đây là java.sql.Timestamp
            flight.setGioDen(rs.getTimestamp("GioDen")); // Đảm bảo đây là java.sql.Timestamp
            flight.setNgayDi(rs.getDate("NgayDi"));
            return flight;
        });
    }

    // Tìm kiếm chuyến bay theo mã chuyến bay
    @SuppressWarnings("deprecation")
    public List<FlightDTO> findFlightsByMaChuyenBay(String maChuyenBay) {
        String query = "SELECT cb.MaChuyenBay, lb.SoHieu AS SoHieuMayBay, lb.MaLoai AS MaLoaiMayBay, " +
                "cb.TenSanBayDi, cb.TenSanBayDen, cb.GioDi, cb.GioDen, lb.NgayDi " +
                "FROM ChuyenBay cb INNER JOIN LichBay lb ON cb.MaChuyenBay = lb.MaChuyenBay " +
                "WHERE cb.MaChuyenBay = ?"; // Câu lệnh SQL được điều chỉnh

        return jdbcTemplate.query(query, new Object[] { maChuyenBay }, (rs, rowNum) -> {
            FlightDTO flight = new FlightDTO(); // Tạo đối tượng FlightSearchDTO
            flight.setMaChuyenBay(rs.getString("MaChuyenBay"));
            flight.setSoHieuMayBay(rs.getString("SoHieuMayBay"));
            flight.setMaLoaiMayBay(rs.getString("MaLoaiMayBay"));
            flight.setTenSanBayDi(rs.getString("TenSanBayDi"));
            flight.setTenSanBayDen(rs.getString("TenSanBayDen"));
            flight.setGioDi(rs.getTimestamp("GioDi")); // Đảm bảo đây là java.sql.Timestamp
            flight.setGioDen(rs.getTimestamp("GioDen")); // Đảm bảo đây là java.sql.Timestamp
            flight.setNgayDi(rs.getDate("NgayDi"));
            return flight; // Trả về đối tượng FlightSearchDTO
        });
    }

    // Tìm kiếm chuyến bay theo sân bay
    @SuppressWarnings("deprecation")
    public List<FlightDTO> findFlightsByAirport(String airportName) {
        String query = "SELECT cb.MaChuyenBay, lb.SoHieu AS SoHieuMayBay, lb.MaLoai AS MaLoaiMayBay, " +
                "cb.TenSanBayDi, cb.TenSanBayDen, cb.GioDi, cb.GioDen, lb.NgayDi " +
                "FROM ChuyenBay cb INNER JOIN LichBay lb ON cb.MaChuyenBay = lb.MaChuyenBay " +
                "WHERE cb.TenSanBayDi = ? OR cb.TenSanBayDen = ?"; // Câu lệnh SQL được điều chỉnh

        return jdbcTemplate.query(query, new Object[] { airportName, airportName }, (rs, rowNum) -> {
            FlightDTO flight = new FlightDTO(); // Tạo đối tượng FlightSearchDTO
            flight.setMaChuyenBay(rs.getString("MaChuyenBay"));
            flight.setSoHieuMayBay(rs.getString("SoHieuMayBay"));
            flight.setMaLoaiMayBay(rs.getString("MaLoaiMayBay"));
            flight.setTenSanBayDi(rs.getString("TenSanBayDi"));
            flight.setTenSanBayDen(rs.getString("TenSanBayDen"));
            flight.setGioDi(rs.getTimestamp("GioDi")); // Đảm bảo đây là java.sql.Timestamp
            flight.setGioDen(rs.getTimestamp("GioDen")); // Đảm bảo đây là java.sql.Timestamp
            flight.setNgayDi(rs.getDate("NgayDi"));
            return flight; // Trả về đối tượng FlightSearchDTO
        });
    }

    // Phương thức đặt chỗ cho chuyến bay
    public boolean bookFlight(Booking booking) {
        String query = "INSERT INTO DatCho (MaKH, MaChuyenBay, NgayDi) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(query, booking.getCustomerId(), booking.getFlightId(), booking.getDepartureDate());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

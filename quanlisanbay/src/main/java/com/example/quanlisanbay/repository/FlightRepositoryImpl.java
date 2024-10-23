package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.FlightDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public class FlightRepositoryImpl implements FlightRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
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
            flight.setGioDi(rs.getTimestamp("GioDi"));  // Đảm bảo đây là java.sql.Timestamp
            flight.setGioDen(rs.getTimestamp("GioDen"));  // Đảm bảo đây là java.sql.Timestamp
            flight.setNgayDi(rs.getDate("NgayDi"));
            return flight;
        });
    }
    
}

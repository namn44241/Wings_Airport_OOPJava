package com.example.quanlisanbay.repository;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class PlaneTypeRepositoryEDIT {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateManufacturer(String planeTypeId, String manufacturer) {
        String query = "UPDATE LoaiMayBay SET HangSanXuat = ? WHERE MaLoai = ?";
        jdbcTemplate.update(query, manufacturer, planeTypeId);
    }
}

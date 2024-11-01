package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.PlaneType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneTypeRepository extends JpaRepository<PlaneType, String> {
    // Các phương thức truy vấn bổ sung có thể được thêm vào nếu cần

    // Lớp nội bộ để thực hiện thao tác cập nhật
    @Repository
    class PlaneTypeRepositoryCustom {
        @Autowired
        private JdbcTemplate jdbcTemplate;

        // Phương thức cập nhật nhà sản xuất của loại máy bay
        public void updateManufacturer(String planeTypeId, String manufacturer) {
            String query = "UPDATE LoaiMayBay SET HangSanXuat = ? WHERE MaLoai = ?";
            jdbcTemplate.update(query, manufacturer, planeTypeId);
        }
    }
}
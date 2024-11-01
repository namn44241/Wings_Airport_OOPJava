package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.PlaneType;
import com.example.quanlisanbay.repository.PlaneTypeRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlaneTypeService {

    @Autowired
    private PlaneTypeRepository planeTypeRepository;

    private final JdbcTemplate jdbcTemplate;

    // Constructor để khởi tạo JdbcTemplate
    @Autowired
    public PlaneTypeService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Phương thức thêm loại máy bay
    public ResponseEntity<?> addPlaneType(PlaneType planeType) {
        try {
            planeTypeRepository.save(planeType);
            return ResponseEntity.ok().body("Thêm loại máy bay thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi thêm loại máy bay: " + e.getMessage());
        }
    }

    // Phương thức xóa loại máy bay
    public void deletePlaneType(String planeTypeId) {
        if (planeTypeRepository.existsById(planeTypeId)) {
            planeTypeRepository.deleteById(planeTypeId);
        } else {
            throw new RuntimeException("Không tìm thấy loại máy bay với ID: " + planeTypeId);
        }
    }

    // Phương thức cập nhật loại máy bay
    public void updatePlaneType(String planeTypeId, String manufacturer) {
        if (planeTypeRepository.existsById(planeTypeId)) {
            PlaneType planeType = planeTypeRepository.findById(planeTypeId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy loại máy bay với ID: " + planeTypeId));
            planeType.setManufacturer(manufacturer);
            planeTypeRepository.save(planeType);
            System.out.println("Cập nhật loại máy bay với ID: " + planeTypeId + " và nhà sản xuất: " + manufacturer);
        } else {
            throw new RuntimeException("Không tìm thấy loại máy bay với ID: " + planeTypeId);
        }
    }

    public List<PlaneType> getAllPlaneTypes() {
        String query = "SELECT MaLoai AS planeTypeId, HangSanXuat AS manufacturer FROM LoaiMayBay";
        return jdbcTemplate.query(query,
                (rs, rowNum) -> new PlaneType(rs.getString("planeTypeId"), rs.getString("manufacturer")));
    }

    // Phương thức lấy mã loại máy bay tiếp theo
    public String getNextPlaneTypeId() {
        String query = "SELECT MAX(MaLoai) FROM LoaiMayBay";
        Integer maxPlaneTypeId = jdbcTemplate.queryForObject(query, Integer.class);

        if (maxPlaneTypeId != null) {
            int nextIdNum = maxPlaneTypeId + 1;
            return String.format("%02d", nextIdNum); // Đảm bảo mã loại có 2 chữ số
        } else {
            return "01"; // Trả về "01" nếu không có loại máy bay nào
        }
    }

}

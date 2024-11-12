package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {
    @Query(value = "SELECT COUNT(*) FROM PhanCong WHERE MaNV = ?1", nativeQuery = true)
    int countPhanCongByMaNV(String maNV);
} 
package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, String> {
    @Query(value = "SELECT MAX(MaKH) FROM KhachHang", nativeQuery = true)
    String findMaxMaKH();
} 
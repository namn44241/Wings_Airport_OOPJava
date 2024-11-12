package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.MayBay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MayBayRepository extends JpaRepository<MayBay, String> {
    boolean existsBySoHieu(String soHieu);
    
    @Query(value = "SELECT COUNT(*) FROM LoaiMayBay WHERE MaLoai = ?1", nativeQuery = true)
    int countLoaiMayBayByMaLoai(String maLoai);
    
    @Query(value = "SELECT COUNT(*) FROM LichBay WHERE SoHieu = ?1", nativeQuery = true)
    int countLichBayBySoHieu(String soHieu);
    
    @Query(value = "SELECT COUNT(*) FROM LoaiMayBay WHERE MaLoai IN (SELECT MaLoai FROM MayBay WHERE SoHieu = ?1)", 
           nativeQuery = true)
    int countLoaiMayBayUsedBySoHieu(String soHieu);
} 
package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.LichBay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LichBayRepository extends JpaRepository<LichBay, String> {
    boolean existsByMaChuyenBay(String maChuyenBay);
    
    @Query(value = "SELECT COUNT(*) FROM DatCho WHERE MaChuyenBay = ?1", nativeQuery = true)
    int countDatChoByMaChuyenBay(String maChuyenBay);
    
    @Query(value = "SELECT COUNT(*) FROM PhanCong WHERE MaChuyenBay = ?1", nativeQuery = true)
    int countPhanCongByMaChuyenBay(String maChuyenBay);
    
    @Query(value = "SELECT MaLoai FROM MayBay WHERE SoHieu = ?1", nativeQuery = true)
    String findMaLoaiByMayBay(String soHieu);
} 
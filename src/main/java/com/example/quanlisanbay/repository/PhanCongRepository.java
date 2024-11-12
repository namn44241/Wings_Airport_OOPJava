package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.PhanCong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PhanCongRepository extends JpaRepository<PhanCong, String> {
    boolean existsByMaNVAndMaChuyenBayAndNgayDi(String maNV, String maChuyenBay, LocalDate ngayDi);
    
    Optional<PhanCong> findByMaNV(String maNV);
    
    @Modifying
    @Query("UPDATE PhanCong p SET p.ngayDi = :newNgayDi, p.maChuyenBay = :newMaChuyenBay " +
           "WHERE p.maNV = :maNV AND p.ngayDi = :oldNgayDi AND p.maChuyenBay = :oldMaChuyenBay")
    int updatePhanCong(String maNV, LocalDate newNgayDi, String newMaChuyenBay, 
                      LocalDate oldNgayDi, String oldMaChuyenBay);
                      
    void deleteByMaNVAndMaChuyenBayAndNgayDi(String maNV, String maChuyenBay, LocalDate ngayDi);
} 
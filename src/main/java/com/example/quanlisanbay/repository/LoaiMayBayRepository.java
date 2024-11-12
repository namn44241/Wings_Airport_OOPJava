package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.LoaiMayBay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiMayBayRepository extends JpaRepository<LoaiMayBay, String> {
    @Query(value = """
        SELECT COUNT(*)
        FROM (
            SELECT MaLoai FROM MayBay WHERE MaLoai = ?1
            UNION ALL
            SELECT MaLoai FROM LichBay WHERE MaLoai = ?1
        ) AS UsedTypes
        """, nativeQuery = true)
    int countUsedTypes(String maLoai);
} 
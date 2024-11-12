package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.ChuyenBay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChuyenBayRepository extends JpaRepository<ChuyenBay, String> {
    @Query(value = """
        SELECT COUNT(*) 
        FROM (
            SELECT MaChuyenBay FROM LichBay WHERE MaChuyenBay = ?1
            UNION ALL
            SELECT MaChuyenBay FROM DatCho WHERE MaChuyenBay = ?1
            UNION ALL 
            SELECT MaChuyenBay FROM PhanCong WHERE MaChuyenBay = ?1
        ) AS UsedFlights
        """, nativeQuery = true)
    int countUsedFlights(String maChuyenBay);
} 
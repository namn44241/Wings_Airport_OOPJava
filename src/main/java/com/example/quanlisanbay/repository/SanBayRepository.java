package com.example.quanlisanbay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface SanBayRepository {
    @Query(value = """
        SELECT 
            cb.MaChuyenBay, 
            COALESCE(mb.SoHieu, 'N/A') as SoHieuMayBay,
            COALESCE(mb.MaLoai, 'N/A') as MaLoaiMayBay,
            cb.TenSanBayDi,
            cb.TenSanBayDen,
            DATE_FORMAT(cb.GioDi, '%Y-%m-%dT%H:%i') as GioDi,
            DATE_FORMAT(cb.GioDen, '%Y-%m-%dT%H:%i') as GioDen
        FROM ChuyenBay cb
        LEFT JOIN LichBay lb ON cb.MaChuyenBay = lb.MaChuyenBay
        LEFT JOIN MayBay mb ON lb.SoHieu = mb.SoHieu
        ORDER BY cb.GioDi DESC
        """, nativeQuery = true)
    List<Map<String, Object>> findAllFlights();

    @Query(value = """
        SELECT 
            cb.MaChuyenBay, 
            COALESCE(mb.SoHieu, 'N/A') as SoHieuMayBay,
            COALESCE(mb.MaLoai, 'N/A') as MaLoaiMayBay,
            cb.TenSanBayDi,
            cb.TenSanBayDen,
            DATE_FORMAT(cb.GioDi, '%Y-%m-%dT%H:%i') as GioDi,
            DATE_FORMAT(cb.GioDen, '%Y-%m-%dT%H:%i') as GioDen
        FROM ChuyenBay cb
        LEFT JOIN LichBay lb ON cb.MaChuyenBay = lb.MaChuyenBay 
        LEFT JOIN MayBay mb ON lb.SoHieu = mb.SoHieu
        WHERE 
            cb.MaChuyenBay LIKE %:query% OR
            mb.SoHieu LIKE %:query% OR 
            mb.MaLoai LIKE %:query% OR
            cb.TenSanBayDi LIKE %:query% OR
            cb.TenSanBayDen LIKE %:query% OR
            cb.GioDi LIKE %:query% OR
            cb.GioDen LIKE %:query%
        ORDER BY cb.GioDi DESC
        """, nativeQuery = true)
    List<Map<String, Object>> searchFlights(String query);
} 
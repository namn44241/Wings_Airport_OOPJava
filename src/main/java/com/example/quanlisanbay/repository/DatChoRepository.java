package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.DatCho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;

@Repository
public interface DatChoRepository extends JpaRepository<DatCho, String> {
    boolean existsByMaKHAndMaChuyenBayAndNgayDi(String maKH, String maChuyenBay, LocalDate ngayDi);
    
    int countByMaKH(String maKH);
    
    @Query(value = """
        SELECT 
            kh.MaKH as MaKH,
            CONCAT(kh.HoDem, ' ', kh.Ten) as HoTen,
            kh.SDT as SDT,
            kh.DiaChi as DiaChi,
            cb.MaChuyenBay as MaChuyenBay,
            cb.TenSanBayDi as TenSanBayDi,
            cb.TenSanBayDen as TenSanBayDen,
            cb.GioDi as GioDi,
            cb.GioDen as GioDen,
            dc.NgayDi as NgayDi
        FROM DatCho dc
        JOIN KhachHang kh ON dc.MaKH = kh.MaKH
        JOIN ChuyenBay cb ON dc.MaChuyenBay = cb.MaChuyenBay
        WHERE dc.MaKH = :maKH 
        AND dc.MaChuyenBay = :maChuyenBay 
        AND dc.NgayDi = :ngayDi
    """, nativeQuery = true)
    Map<String, Object> findBookingDetails(
        @Param("maKH") String maKH,
        @Param("maChuyenBay") String maChuyenBay,
        @Param("ngayDi") LocalDate ngayDi
    );
}
package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.FlightDELETE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDELETERepository extends JpaRepository<FlightDELETE, String> {
    @Query(value = """
            SELECT COUNT(*) FROM (
                SELECT MaChuyenBay FROM LichBay WHERE MaChuyenBay = :flightId
                UNION ALL
                SELECT MaChuyenBay FROM DatCho WHERE MaChuyenBay = :flightId
                UNION ALL
                SELECT MaChuyenBay FROM PhanCong WHERE MaChuyenBay = :flightId
            ) AS UsedFlights
            """, nativeQuery = true)
    int countFlightUsages(@Param("flightId") String flightId);
}
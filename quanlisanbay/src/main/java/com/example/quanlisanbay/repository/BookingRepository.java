package com.example.quanlisanbay.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.quanlisanbay.model.Booking;

@Repository
public class BookingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Đảm bảo rằng phương thức này đã được khai báo và thực hiện đúng
    public boolean bookFlight(Booking booking) {
        String query = "INSERT INTO DatCho (MaKH, MaChuyenBay, NgayDi) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(query, booking.getCustomerId(), booking.getFlightId(), booking.getDepartureDate());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

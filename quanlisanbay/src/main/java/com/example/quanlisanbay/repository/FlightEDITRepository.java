package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.FlightEDIT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightEDITRepository extends JpaRepository<FlightEDIT, String> {
    // Có thể thêm các truy vấn tùy chỉnh ở đây nếu cần
}

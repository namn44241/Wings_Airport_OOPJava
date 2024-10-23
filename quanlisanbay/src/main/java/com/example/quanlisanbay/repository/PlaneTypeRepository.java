package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.PlaneType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneTypeRepository extends JpaRepository<PlaneType, String> {
    // Các phương thức truy vấn có thể được thêm vào nếu cần
}

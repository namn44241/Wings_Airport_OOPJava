package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.PlaneType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneTypeRepositoryDELETE extends JpaRepository<PlaneType, String> {
    // Có thể thêm các phương thức truy vấn tùy chỉnh ở đây nếu cần
}

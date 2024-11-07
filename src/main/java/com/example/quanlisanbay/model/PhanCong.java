package com.example.quanlisanbay.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PhanCong")
public class PhanCong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "MaNV")
    private String maNV;
    
    @Column(name = "MaChuyenBay")
    private String maChuyenBay;
    
    @Column(name = "NgayDi")
    private LocalDate ngayDi;

    // Constructors, getters, setters
}
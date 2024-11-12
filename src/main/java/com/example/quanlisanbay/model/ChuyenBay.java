package com.example.quanlisanbay.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ChuyenBay")
public class ChuyenBay {
    @Id
    @Column(name = "MaChuyenBay")
    private String maChuyenBay;
    
    @Column(name = "TenSanBayDi")
    private String tenSanBayDi;
    
    @Column(name = "TenSanBayDen")
    private String tenSanBayDen;
    
    @Column(name = "GioDi")
    private LocalDateTime gioDi;
    
    @Column(name = "GioDen")
    private LocalDateTime gioDen;

    // Getters and Setters
}
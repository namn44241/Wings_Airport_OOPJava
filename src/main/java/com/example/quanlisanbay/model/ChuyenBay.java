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

    // Thêm constructor
    public ChuyenBay() {}
    
    public ChuyenBay(String maChuyenBay, String tenSanBayDi, String tenSanBayDen, 
                     LocalDateTime gioDi, LocalDateTime gioDen) {
        this.maChuyenBay = maChuyenBay;
        this.tenSanBayDi = tenSanBayDi;
        this.tenSanBayDen = tenSanBayDen;
        this.gioDi = gioDi;
        this.gioDen = gioDen;
    }

    // Thêm getters và setters
    public String getMaChuyenBay() { return maChuyenBay; }
    public void setMaChuyenBay(String maChuyenBay) { this.maChuyenBay = maChuyenBay; }

    public String getTenSanBayDi() { return tenSanBayDi; }
    public void setTenSanBayDi(String tenSanBayDi) { this.tenSanBayDi = tenSanBayDi; }

    public String getTenSanBayDen() { return tenSanBayDen; }
    public void setTenSanBayDen(String tenSanBayDen) { this.tenSanBayDen = tenSanBayDen; }

    public LocalDateTime getGioDi() { return gioDi; }
    public void setGioDi(LocalDateTime gioDi) { this.gioDi = gioDi; }

    public LocalDateTime getGioDen() { return gioDen; }
    public void setGioDen(LocalDateTime gioDen) { this.gioDen = gioDen; }
}
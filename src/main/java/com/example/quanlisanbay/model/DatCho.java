package com.example.quanlisanbay.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DatCho")
public class DatCho {
    @Id
    @Column(name = "MaKH")
    private String maKH;
    
    @Column(name = "MaChuyenBay")
    private String maChuyenBay;
    
    @Column(name = "NgayDi")
    private LocalDate ngayDi;

    // Constructors
    public DatCho() {}
    
    public DatCho(String maKH, String maChuyenBay, LocalDate ngayDi) {
        this.maKH = maKH;
        this.maChuyenBay = maChuyenBay;
        this.ngayDi = ngayDi;
    }

    // Getters and Setters
    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaChuyenBay() {
        return maChuyenBay;
    }

    public void setMaChuyenBay(String maChuyenBay) {
        this.maChuyenBay = maChuyenBay;
    }

    public LocalDate getNgayDi() {
        return ngayDi;
    }

    public void setNgayDi(LocalDate ngayDi) {
        this.ngayDi = ngayDi;
    }
} 
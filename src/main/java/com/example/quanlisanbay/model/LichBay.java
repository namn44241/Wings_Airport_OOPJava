package com.example.quanlisanbay.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "LichBay")
public class LichBay {
    @Id
    @Column(name = "MaChuyenBay")
    private String maChuyenBay;
    
    @Column(name = "NgayDi")
    private LocalDate ngayDi;
    
    @Column(name = "SoHieu")
    private String soHieu;
    
    @Column(name = "MaLoai")
    private String maLoai;

    // Constructors
    public LichBay() {}
    
    public LichBay(String maChuyenBay, LocalDate ngayDi, String soHieu, String maLoai) {
        this.maChuyenBay = maChuyenBay;
        this.ngayDi = ngayDi;
        this.soHieu = soHieu;
        this.maLoai = maLoai;
    }

    // Getters and Setters
    public String getMaChuyenBay() { return maChuyenBay; }
    public void setMaChuyenBay(String maChuyenBay) { this.maChuyenBay = maChuyenBay; }

    public LocalDate getNgayDi() { return ngayDi; }
    public void setNgayDi(LocalDate ngayDi) { this.ngayDi = ngayDi; }

    public String getSoHieu() { return soHieu; }
    public void setSoHieu(String soHieu) { this.soHieu = soHieu; }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }
} 
package com.example.quanlisanbay.model;

import jakarta.persistence.*;

@Entity
@Table(name = "LoaiMayBay")
public class LoaiMayBay {
    @Id
    @Column(name = "MaLoai")
    private String maLoai;
    
    @Column(name = "HangSanXuat")
    private String hangSanXuat;

    // Constructors
    public LoaiMayBay() {}
    
    public LoaiMayBay(String maLoai, String hangSanXuat) {
        this.maLoai = maLoai;
        this.hangSanXuat = hangSanXuat;
    }

    // Getters and Setters
    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    public String getHangSanXuat() { return hangSanXuat; }
    public void setHangSanXuat(String hangSanXuat) { this.hangSanXuat = hangSanXuat; }
} 
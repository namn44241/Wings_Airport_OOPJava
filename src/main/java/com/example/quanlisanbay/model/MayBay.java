package com.example.quanlisanbay.model;

import jakarta.persistence.*;

@Entity
@Table(name = "MayBay")
public class MayBay {
    @Id
    @Column(name = "SoHieu", length = 10)
    private String soHieu;
    
    @Column(name = "MaLoai")
    private String maLoai;
    
    @Column(name = "SoGheNgoi")
    private Integer soGheNgoi;

    // Constructors
    public MayBay() {}
    
    public MayBay(String soHieu, String maLoai, Integer soGheNgoi) {
        this.soHieu = soHieu;
        this.maLoai = maLoai;
        this.soGheNgoi = soGheNgoi;
    }

    // Getters and Setters
    public String getSoHieu() { return soHieu; }
    public void setSoHieu(String soHieu) { this.soHieu = soHieu; }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    public Integer getSoGheNgoi() { return soGheNgoi; }
    public void setSoGheNgoi(Integer soGheNgoi) { this.soGheNgoi = soGheNgoi; }
} 
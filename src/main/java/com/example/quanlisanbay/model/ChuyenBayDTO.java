package com.example.quanlisanbay.model;

import java.time.LocalDateTime;

public class ChuyenBayDTO {
    private String maChuyenBay;
    private String soHieuMayBay;
    private String maLoaiMayBay;
    private String tenSanBayDi;
    private String tenSanBayDen;
    private LocalDateTime gioDi;
    private LocalDateTime gioDen;

    // Constructors
    public ChuyenBayDTO() {}
    
    // Getters and Setters
    public String getMaChuyenBay() { return maChuyenBay; }
    public void setMaChuyenBay(String maChuyenBay) { this.maChuyenBay = maChuyenBay; }

    public String getSoHieuMayBay() { return soHieuMayBay; }
    public void setSoHieuMayBay(String soHieuMayBay) { this.soHieuMayBay = soHieuMayBay; }

    public String getMaLoaiMayBay() { return maLoaiMayBay; }
    public void setMaLoaiMayBay(String maLoaiMayBay) { this.maLoaiMayBay = maLoaiMayBay; }

    public String getTenSanBayDi() { return tenSanBayDi; }
    public void setTenSanBayDi(String tenSanBayDi) { this.tenSanBayDi = tenSanBayDi; }

    public String getTenSanBayDen() { return tenSanBayDen; }
    public void setTenSanBayDen(String tenSanBayDen) { this.tenSanBayDen = tenSanBayDen; }

    public LocalDateTime getGioDi() { return gioDi; }
    public void setGioDi(LocalDateTime gioDi) { this.gioDi = gioDi; }

    public LocalDateTime getGioDen() { return gioDen; }
    public void setGioDen(LocalDateTime gioDen) { this.gioDen = gioDen; }
} 
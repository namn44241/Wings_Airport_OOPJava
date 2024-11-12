package com.example.quanlisanbay.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "NhanVien")
public class NhanVien {
    @Id
    @Column(name = "MaNV")
    private String maNV;
    
    @Column(name = "HoDem")
    private String hoDem;
    
    @Column(name = "Ten")
    private String ten;
    
    @Column(name = "SDT")
    private String sdt;
    
    @Column(name = "DiaChi")
    private String diaChi;
    
    @Column(name = "Luong")
    private BigDecimal luong;
    
    @Column(name = "LoaiNV")
    private String loaiNV;

    // Constructors
    public NhanVien() {}
    
    public NhanVien(String maNV, String hoDem, String ten, String sdt, 
                    String diaChi, BigDecimal luong, String loaiNV) {
        this.maNV = maNV;
        this.hoDem = hoDem;
        this.ten = ten;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.luong = luong;
        this.loaiNV = loaiNV;
    }

    // Getters and Setters
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoDem() { return hoDem; }
    public void setHoDem(String hoDem) { this.hoDem = hoDem; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public BigDecimal getLuong() { return luong; }
    public void setLuong(BigDecimal luong) { this.luong = luong; }

    public String getLoaiNV() { return loaiNV; }
    public void setLoaiNV(String loaiNV) { this.loaiNV = loaiNV; }
} 
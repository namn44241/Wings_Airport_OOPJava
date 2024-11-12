package com.example.quanlisanbay.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;


@Entity
@Table(name = "KhachHang")
public class KhachHang {
    @Id
    @Column(name = "MaKH")
    private String maKH;
    
    @Column(name = "SDT")
    private String sdt;
    
    @Column(name = "HoDem")
    private String hoDem;
    
    @Column(name = "Ten")
    private String ten;
    
    @Column(name = "DiaChi")
    private String diaChi;

    // Constructors
    public KhachHang() {}
    
    public KhachHang(String maKH, String sdt, String hoDem, String ten, String diaChi) {
        this.maKH = maKH;
        this.sdt = sdt;
        this.hoDem = hoDem;
        this.ten = ten;
        this.diaChi = diaChi;
    }

    // Getters and Setters
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getHoDem() { return hoDem; }
    public void setHoDem(String hoDem) { this.hoDem = hoDem; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
}
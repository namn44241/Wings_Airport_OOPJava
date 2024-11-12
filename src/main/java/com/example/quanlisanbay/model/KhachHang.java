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
    
    @Column(name = "HoDem")
    private String hoDem;
    
    @Column(name = "Ten")
    private String ten;
    
    @Column(name = "SDT")
    private String sdt;
    
    @Column(name = "DiaChi")
    private String diaChi;

    // Getters and setters
} 
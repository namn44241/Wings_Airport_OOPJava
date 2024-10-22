package com.example.quanlisanbay;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LoaiMayBay")
public class LoaiMayBay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MaLoai")
    private int maLoai;

    private String hangSanXuat;

    public int getMaLoai(){
        return maLoai;
    }

    public void setMaLoai(int maLoai){
        this.maLoai=maLoai;
    }

    public String getHangSanXuat(){
        return hangSanXuat;
    } 

    public void setHangSanXuat(String hangSanXuat){
        this.hangSanXuat=hangSanXuat;
    }
}

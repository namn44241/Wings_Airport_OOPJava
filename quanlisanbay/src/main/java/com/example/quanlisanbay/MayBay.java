package com.example.quanlisanbay;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "MayBay")
public class MayBay {
    @Id
    private String soHieu;

    @ManyToOne
    @JoinColumn(name = "MaLoai")
    private LoaiMayBay loaiMayBay;

    private int soGheNgoi;

    public String getSoHieu(){
        return soHieu;
    }

    public void setSoHieu(String soHieu){
        this.soHieu=soHieu;
    }

    public LoaiMayBay getLoaiMayBay(){
        return loaiMayBay;
    }
    
    public void setLoaiMayBay(LoaiMayBay loaiMayBay){
        this.loaiMayBay=loaiMayBay;
    }

    public Integer getSoGheNgoi(){
        return soGheNgoi;
    }

    public void setSoGheNgoi(Integer soGheNgoi){
        this.soGheNgoi=soGheNgoi;
    }
    
}

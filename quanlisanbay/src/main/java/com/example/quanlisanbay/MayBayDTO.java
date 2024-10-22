package com.example.quanlisanbay;

public class MayBayDTO {
    private String soHieu;
    private Integer maLoai;
    private Integer soGheNgoi;

    public String getSoHieu(){
        return soHieu;
    } 

    public void setSoHieu(String soHieu){
        this.soHieu=soHieu;
    }

    public int getMaLoai(){
        return maLoai;
    }

    public void setMaLoai(Integer maLoai){
        this.maLoai=maLoai;
    }

    public int getSoGheNgoi(){
        return soGheNgoi;
    }

    public void setSoGheNgoi(int soGheNgoi){
        this.soGheNgoi=soGheNgoi;
    }
}

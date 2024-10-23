package com.example.quanlisanbay.model;

import java.sql.Timestamp;

public class FlightSearchDTO {
    private String maChuyenBay;
    private String tenSanBayDi;
    private String tenSanBayDen;
    private Timestamp gioDi;
    private Timestamp gioDen;

    // Getter and Setter
    public String getMaChuyenBay() {
        return maChuyenBay;
    }

    public void setMaChuyenBay(String maChuyenBay) {
        this.maChuyenBay = maChuyenBay;
    }

    public String getTenSanBayDi() {
        return tenSanBayDi;
    }

    public void setTenSanBayDi(String tenSanBayDi) {
        this.tenSanBayDi = tenSanBayDi;
    }

    public String getTenSanBayDen() {
        return tenSanBayDen;
    }

    public void setTenSanBayDen(String tenSanBayDen) {
        this.tenSanBayDen = tenSanBayDen;
    }

    public Timestamp getGioDi() {
        return gioDi;
    }

    public void setGioDi(Timestamp gioDi) {
        this.gioDi = gioDi;
    }

    public Timestamp getGioDen() {
        return gioDen;
    }

    public void setGioDen(Timestamp gioDen) {
        this.gioDen = gioDen;
    }
}

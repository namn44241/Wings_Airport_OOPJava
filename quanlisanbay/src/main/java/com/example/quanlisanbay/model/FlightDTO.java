package com.example.quanlisanbay.model;

import java.sql.Timestamp; // Sử dụng java.sql.Timestamp thay vì java.security.Timestamp
import java.util.Date;

public class FlightDTO {
    private String maChuyenBay;
    private String soHieuMayBay;
    private String maLoaiMayBay;
    private String tenSanBayDi;
    private String tenSanBayDen;
    private Timestamp gioDi; // Thay thế kiểu dữ liệu thành java.sql.Timestamp
    private Timestamp gioDen; // Thay thế kiểu dữ liệu thành java.sql.Timestamp
    private Date ngayDi;

    // Các phương thức setter
    public void setMaChuyenBay(String maChuyenBay) {
        this.maChuyenBay = maChuyenBay;
    }

    public void setSoHieuMayBay(String soHieuMayBay) {
        this.soHieuMayBay = soHieuMayBay;
    }

    public void setMaLoaiMayBay(String maLoaiMayBay) {
        this.maLoaiMayBay = maLoaiMayBay;
    }

    public void setTenSanBayDi(String tenSanBayDi) {
        this.tenSanBayDi = tenSanBayDi;
    }

    public void setTenSanBayDen(String tenSanBayDen) {
        this.tenSanBayDen = tenSanBayDen;
    }

    public void setGioDi(Timestamp gioDi) {
        this.gioDi = gioDi;
    }

    public void setGioDen(Timestamp gioDen) {
        this.gioDen = gioDen;
    }

    public void setNgayDi(Date ngayDi) {
        this.ngayDi = ngayDi;
    }

    // Các phương thức getter
    public String getMaChuyenBay() {
        return maChuyenBay;
    }

    public String getSoHieuMayBay() {
        return soHieuMayBay;
    }

    public String getMaLoaiMayBay() {
        return maLoaiMayBay;
    }

    public String getTenSanBayDi() {
        return tenSanBayDi;
    }

    public String getTenSanBayDen() {
        return tenSanBayDen;
    }

    public Timestamp getGioDi() {
        return gioDi;
    }

    public Timestamp getGioDen() {
        return gioDen;
    }

    public Date getNgayDi() {
        return ngayDi;
    }
}

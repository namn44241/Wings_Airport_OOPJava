package com.example.quanlisanbay.service;

import org.springframework.stereotype.Service;

@Service
public class PlaneTypeServiceEDIT {

    // Phương thức cập nhật loại máy bay
    public void updatePlaneType(String planeTypeId, String manufacturer) {
        // Logic cập nhật loại máy bay
        System.out.println("Cập nhật loại máy bay với ID: " + planeTypeId + " và nhà sản xuất: " + manufacturer);

        // Giả sử bạn có logic để cập nhật loại máy bay vào cơ sở dữ liệu
        // (Ví dụ: sử dụng một repository hoặc cách khác để thực hiện thao tác này)
    }
}
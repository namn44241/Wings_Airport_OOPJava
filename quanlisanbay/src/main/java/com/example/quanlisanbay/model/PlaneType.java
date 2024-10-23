package com.example.quanlisanbay.model;

import javax.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PlaneType {

    @Id
    @NotNull
    private String planeTypeId;

    @NotNull
    private String manufacturer;

    // Getters and Setters
    public String getPlaneTypeId() {
        return planeTypeId;
    }

    public void setPlaneTypeId(String planeTypeId) {
        this.planeTypeId = planeTypeId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}

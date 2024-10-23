package com.example.quanlisanbay.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PlaneTypeDELETE {

    @Id
    private String planeTypeId;
    private String name;

    // Getters và Setters

    public String getPlaneTypeId() {
        return planeTypeId;
    }

    public void setPlaneTypeId(String planeTypeId) {
        this.planeTypeId = planeTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "PlaneType{" +
                "planeTypeId='" + planeTypeId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

package com.example.quanlisanbay.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FlightDELETE {

    @Id
    private String flightId;
    private String flightName;

    // Các getter và setter
    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }
}

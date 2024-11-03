package com.example.quanlisanbay.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity

public class BookingDTO {
    @Id
    private String customerId; // Mã khách hàng
    private String flightId; // Mã chuyến bay
    private LocalDate departureDate; // Ngày đi

    // Constructor không tham số
    public BookingDTO() {
    }

    // Constructor đầy đủ
    public BookingDTO(String customerId, String flightId, LocalDate departureDate) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.departureDate = departureDate;
    }

    // Getters và Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
}

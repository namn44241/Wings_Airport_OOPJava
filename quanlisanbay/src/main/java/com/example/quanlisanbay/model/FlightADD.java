package com.example.quanlisanbay.model;
import java.time.LocalDateTime;
public class FlightADD {
    private String flightId;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public FlightADD() {
    }
    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }
    public String getFlightId() {
        return flightId;
    }
    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }
    public String getDepartureAirport() {
        return departureAirport;
    }
    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }
    public String getArrivalAirport() {
        return arrivalAirport;
    }
    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
}

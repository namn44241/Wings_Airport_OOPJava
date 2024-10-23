package com.example.quanlisanbay.repository;

import com.example.quanlisanbay.model.FlightDTO;
import java.util.List;

public interface FlightRepository {
    List<FlightDTO> findAllFlights();
}

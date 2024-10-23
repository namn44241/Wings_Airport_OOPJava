package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.FlightDTO;
import com.example.quanlisanbay.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Override
    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAllFlights();
    }
}

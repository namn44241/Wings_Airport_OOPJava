package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.FlightSearchDTO;
import com.example.quanlisanbay.repository.FlightSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FlightSearchService {

    @Autowired
    private FlightSearchRepository flightSearchRepository;

    public List<FlightSearchDTO> searchFlights(String query, String searchType) {
        if ("flight".equalsIgnoreCase(searchType)) {
            return flightSearchRepository.findFlightsByMaChuyenBay(query);
        } else {
            return flightSearchRepository.findFlightsByAirport(query);
        }
    }
}

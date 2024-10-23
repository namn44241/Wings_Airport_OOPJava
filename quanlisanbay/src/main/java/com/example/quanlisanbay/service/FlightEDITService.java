package com.example.quanlisanbay.service;

import com.example.quanlisanbay.model.FlightEDIT;
import com.example.quanlisanbay.repository.FlightEDITRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FlightEDITService {

    @Autowired
    private FlightEDITRepository flightEDITRepository;

    @Transactional
    public boolean updateFlight(String flightId, String departureAirport, String arrivalAirport, LocalDateTime depTime,
            LocalDateTime arrTime) {
        try {
            // Lấy chuyến bay từ cơ sở dữ liệu
            FlightEDIT flightEDIT = flightEDITRepository.findById(flightId).orElse(null);

            if (flightEDIT == null) {
                // Log lỗi nếu không tìm thấy chuyến bay
                System.out.println("Flight not found with ID: " + flightId);
                return false;
            }

            // Kiểm tra sân bay đi và đến
            if (departureAirport.equals(arrivalAirport)) {
                System.out.println("Departure and Arrival airports cannot be the same.");
                return false;
            }

            // Log thời gian cập nhật
            System.out.println("Updating flight with departure time: " + depTime + " and arrival time: " + arrTime);

            // Cập nhật thông tin chuyến bay
            flightEDIT.setDepartureAirport(departureAirport);
            flightEDIT.setArrivalAirport(arrivalAirport);
            flightEDIT.setDepartureTime(depTime);
            flightEDIT.setArrivalTime(arrTime);

            // Lưu lại chuyến bay đã được cập nhật
            flightEDITRepository.save(flightEDIT);
            return true;
        } catch (Exception e) {
            // Log chi tiết lỗi
            e.printStackTrace();
            return false;
        }
    }
}

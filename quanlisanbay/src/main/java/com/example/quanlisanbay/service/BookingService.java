package com.example.quanlisanbay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.quanlisanbay.model.Booking;
import com.example.quanlisanbay.repository.BookingRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // Phương thức đặt chỗ
    public boolean bookFlight(Booking booking) {
        try {
            return bookingRepository.bookFlight(booking);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

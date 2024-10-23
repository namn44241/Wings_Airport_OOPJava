package com.example.quanlisanbay.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FlightDELETEService {

    // Giả sử bạn gọi đến repository để thực hiện xóa
    public ResponseEntity<?> deleteFlightById(String flightId) {
        // flightId từ FlightDELETE model sẽ được sử dụng để xóa chuyến bay
        // flightRepository.deleteById(flightId); // Giả sử bạn sử dụng JPA repository
        // để xóa

        // Trả về thông báo sau khi xóa thành công
        return ResponseEntity.ok("Flight with ID " + flightId + " has been deleted successfully.");
    }
}

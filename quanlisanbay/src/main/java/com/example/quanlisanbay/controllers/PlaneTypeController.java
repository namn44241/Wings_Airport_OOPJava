package com.example.quanlisanbay.controllers;

import com.example.quanlisanbay.model.PlaneType;
import com.example.quanlisanbay.service.PlaneTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlaneTypeController {

    @Autowired
    private PlaneTypeService planeTypeService;

    // API thêm loại máy bay
    @PostMapping("/them_loai_mb")
    public ResponseEntity<Map<String, String>> addPlaneType(@Valid @RequestBody PlaneType planeType) {
        Map<String, String> response = new HashMap<>();
        try {
            // Giả sử bạn gọi một phương thức trong service để thêm loại máy bay
            planeTypeService.addPlaneType(planeType);

            // Trả về phản hồi thành công
            response.put("status", "success");
            response.put("message", "Thêm loại máy bay thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Trả về phản hồi lỗi dưới dạng JSON
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // API xóa loại máy bay
    @DeleteMapping("/xoa_loai_mb/{planeTypeId}")
    public ResponseEntity<String> deletePlaneType(@PathVariable String planeTypeId) {
        try {
            planeTypeService.deletePlaneType(planeTypeId);
            return ResponseEntity.ok("Loại máy bay đã được xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Không thể xóa loại máy bay: " + e.getMessage());
        }
    }

    // API cập nhật loại máy bay
    @PutMapping("/sua_loai_mb/{planeTypeId}")
    public ResponseEntity<?> updatePlaneType(
            @PathVariable("planeTypeId") String planeTypeId,
            @RequestParam("manufacturer") String manufacturer) {
        try {
            planeTypeService.updatePlaneType(planeTypeId, manufacturer);
            return ResponseEntity.ok().body("Cập nhật thông tin loại máy bay thành công");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi cập nhật thông tin loại máy bay: " + e.getMessage());
        }
    }

    // API lấy danh sách tất cả các loại máy bay
    @GetMapping("/get_plane_types")
    public List<PlaneType> getAllPlaneTypes() {
        return planeTypeService.getAllPlaneTypes();
    }

    // API lấy mã loại máy bay tiếp theo
    @GetMapping("/next_plane_type_id")
    public Map<String, String> getNextPlaneTypeId() {
        String nextId = planeTypeService.getNextPlaneTypeId();
        Map<String, String> response = new HashMap<>();
        response.put("nextPlaneTypeId", nextId);
        return response;
    }
}

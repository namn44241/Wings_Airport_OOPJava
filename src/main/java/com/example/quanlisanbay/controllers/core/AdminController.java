package com.example.quanlisanbay.controllers.core;

import com.example.quanlisanbay.config.LoginRequired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
   
   @Autowired 
   private JdbcTemplate jdbcTemplate;
   
   // Trang admin chính
   @GetMapping("/admin")
   @LoginRequired 
   public String admin(Model model, HttpSession session) {
       String username = (String) session.getAttribute("username");
       if (username == null) {
           return "redirect:/login";
       }
       Map<String, Object> stats = getStats();
       model.addAttribute("stats", stats);
       model.addAttribute("username", username);
       
       return "admin";
   }

   // API lấy thông tin chuyến bay
   @GetMapping("/api/admin/flights")
   @ResponseBody
   public Map<String, Object> getFlights() {
       Map<String, Object> response = new HashMap<>();
       
       // Danh sách chuyến bay
       String flightQuery = "SELECT MaChuyenBay, TenSanBayDi, TenSanBayDen, GioDi, GioDen FROM ChuyenBay";
       List<Map<String, Object>> flightInfo = jdbcTemplate.queryForList(flightQuery);
       
       // Mã chuyến bay tiếp theo
       String maxFlightIdQuery = "SELECT MAX(MaChuyenBay) FROM ChuyenBay";
       String maxFlightId = jdbcTemplate.queryForObject(maxFlightIdQuery, String.class);
       String nextFlightId = maxFlightId != null ? 
           String.format("CB%06d", Integer.parseInt(maxFlightId.substring(2)) + 1) : 
           "CB000001";

       response.put("flightInfo", flightInfo);
       response.put("nextFlightId", nextFlightId);
       return response;
   }

   // API lấy thông tin máy bay
   @GetMapping("/api/admin/aircraft")
   @ResponseBody
   public Map<String, Object> getAircraft() {
       Map<String, Object> response = new HashMap<>();
       
       // Thông tin loại máy bay
       String aircraftQuery = "SELECT MaLoai, HangSanXuat FROM LoaiMayBay";
       List<Map<String, Object>> aircraftInfo = jdbcTemplate.queryForList(aircraftQuery);

       // Mã loại tiếp theo  
       String maxPlaneTypeIdQuery = "SELECT MAX(MaLoai) FROM LoaiMayBay";
       String maxPlaneTypeId = jdbcTemplate.queryForObject(maxPlaneTypeIdQuery, String.class);
       String nextPlaneTypeId = maxPlaneTypeId != null ?
           String.format("%02d", Integer.parseInt(maxPlaneTypeId) + 1) :
           "01";

       // Thông tin máy bay
       String planeQuery = """
           SELECT mb.SoHieu, mb.MaLoai, lmb.HangSanXuat, mb.SoGheNgoi 
           FROM MayBay mb 
           JOIN LoaiMayBay lmb ON mb.MaLoai = lmb.MaLoai
           """;
       List<Map<String, Object>> planeInfo = jdbcTemplate.queryForList(planeQuery);

       response.put("aircraftInfo", aircraftInfo);
       response.put("nextPlaneTypeId", nextPlaneTypeId); 
       response.put("planeInfo", planeInfo);
       return response;
   }

   // API lấy thông tin đặt chỗ và khách hàng 
   @GetMapping("/api/admin/bookings")
   @ResponseBody
   public Map<String, Object> getBookings() {
       Map<String, Object> response = new HashMap<>();
       
       // Thông tin đặt chỗ
       String bookingQuery = """
           SELECT dc.MaKH, kh.HoDem, kh.Ten, kh.SDT, dc.NgayDi, cb.MaChuyenBay,
               cb.GioDi, cb.GioDen, cb.TenSanBayDi, cb.TenSanBayDen
           FROM DatCho dc
           JOIN ChuyenBay cb ON dc.MaChuyenBay = cb.MaChuyenBay
           JOIN KhachHang kh ON dc.MaKH = kh.MaKH
           """;
       List<Map<String, Object>> bookingInfo = jdbcTemplate.queryForList(bookingQuery);

       // Thông tin khách hàng
       String customerQuery = "SELECT MaKH, SDT, HoDem, Ten, DiaChi FROM KhachHang";
       List<Map<String, Object>> customerInfo = jdbcTemplate.queryForList(customerQuery);

       // Mã khách hàng tiếp theo
       String maxCustomerIdQuery = "SELECT MAX(MaKH) FROM KhachHang";
       String maxCustomerId = jdbcTemplate.queryForObject(maxCustomerIdQuery, String.class);
       String nextCustomerId = maxCustomerId != null ?
           String.format("KH%06d", Integer.parseInt(maxCustomerId.substring(2)) + 1) :
           "KH000001";

       response.put("bookingInfo", bookingInfo);
       response.put("customerInfo", customerInfo);
       response.put("nextCustomerId", nextCustomerId);
       return response;
   }
   
    @GetMapping("/api/admin/flight-details")
    @ResponseBody 
    public Map<String, Object> getFlightDetails(@RequestParam String flight_id) {
        String query = "SELECT GioDi as departure_time, GioDen as arrival_time FROM ChuyenBay WHERE MaChuyenBay = ?";
        return jdbcTemplate.queryForMap(query, flight_id);
    }
    
   // API lấy thông tin nhân viên và phân công
   @GetMapping("/api/admin/employees") 
   @ResponseBody
   public Map<String, Object> getEmployees() {
       Map<String, Object> response = new HashMap<>();
       
       // Thông tin nhân viên
       String employeeQuery = "SELECT MaNV, HoDem, Ten, SDT, DiaChi, Luong, LoaiNV FROM NhanVien";
       List<Map<String, Object>> employeeInfo = jdbcTemplate.queryForList(employeeQuery);

       // Mã nhân viên tiếp theo
       String maxEmployeeIdQuery = "SELECT MAX(MaNV) FROM NhanVien";
       String maxEmployeeId = jdbcTemplate.queryForObject(maxEmployeeIdQuery, String.class);
       String nextEmployeeId = maxEmployeeId != null ?
           String.format("NV%06d", Integer.parseInt(maxEmployeeId.substring(2)) + 1) :
           "NV000001";

       // Thông tin phân công
       String assignmentQuery = """
           SELECT p.MaNV, CONCAT(n.HoDem, ' ', n.Ten) AS HoTen, n.SDT, n.LoaiNV,
               p.MaChuyenBay, c.TenSanBayDi, c.TenSanBayDen, c.GioDi, c.GioDen, p.NgayDi
           FROM PhanCong p
           JOIN NhanVien n ON p.MaNV = n.MaNV
           JOIN ChuyenBay c ON p.MaChuyenBay = c.MaChuyenBay
           """;
       List<Map<String, Object>> assignmentInfo = jdbcTemplate.queryForList(assignmentQuery);

       response.put("employeeInfo", employeeInfo);
       response.put("nextEmployeeId", nextEmployeeId);
       response.put("assignmentInfo", assignmentInfo);
       return response;
   }

   // API lấy thống kê
   @GetMapping("/api/admin/stats")
   @ResponseBody 
   public Map<String, Object> getStats() {
       Map<String, Object> stats = new HashMap<>();
       
       // Thống kê số lượng 
       stats.put("khach_hang", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM KhachHang", Integer.class));
       stats.put("nhan_vien", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM NhanVien", Integer.class));
       stats.put("loai_may_bay", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM LoaiMayBay", Integer.class));
       stats.put("may_bay", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MayBay", Integer.class));
       stats.put("chuyen_bay", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ChuyenBay", Integer.class));
       stats.put("lich_bay", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM LichBay", Integer.class));
       stats.put("dat_cho", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM DatCho", Integer.class));
       stats.put("phan_cong", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM PhanCong", Integer.class));
       

       // Thống kê loại máy bay
       String planeStatsQuery = "SELECT HangSanXuat, COUNT(*) as count FROM LoaiMayBay GROUP BY HangSanXuat";
       List<Map<String, Object>> planeStats = jdbcTemplate.queryForList(planeStatsQuery);
       Map<String, Object> loaiMayBayStats = new HashMap<>();
       List<String> labels = new ArrayList<>();
       List<Integer> data = new ArrayList<>();
       for (Map<String, Object> stat : planeStats) {
           labels.add((String) stat.get("HangSanXuat"));
           data.add(((Number) stat.get("count")).intValue());
       }
       loaiMayBayStats.put("labels", labels);
       loaiMayBayStats.put("data", data);
       stats.put("loai_may_bay_stats", loaiMayBayStats);

       // Top chuyến bay
       String topFlightsQuery = """
           SELECT cb.MaChuyenBay, cb.TenSanBayDi, cb.TenSanBayDen, COUNT(dc.MaKH) as total_bookings
           FROM ChuyenBay cb
           JOIN LichBay lb ON lb.MaChuyenBay = cb.MaChuyenBay
           JOIN DatCho dc ON dc.MaChuyenBay = lb.MaChuyenBay AND dc.NgayDi = lb.NgayDi
           GROUP BY cb.MaChuyenBay, cb.TenSanBayDi, cb.TenSanBayDen
           ORDER BY total_bookings DESC
           LIMIT 5
           """;
       List<Map<String, Object>> topFlights = jdbcTemplate.queryForList(topFlightsQuery);
       Map<String, Object> topChuyenBayStats = new HashMap<>();
       labels = new ArrayList<>();
       data = new ArrayList<>();
       for (Map<String, Object> flight : topFlights) {
           labels.add(String.format("%s (%s -> %s)", 
               flight.get("MaChuyenBay"),
               flight.get("TenSanBayDi"),
               flight.get("TenSanBayDen")));
           data.add(((Number) flight.get("total_bookings")).intValue());
       }
       topChuyenBayStats.put("labels", labels);
       topChuyenBayStats.put("data", data);
       stats.put("top_chuyen_bay", topChuyenBayStats);

       // Thống kê nhân viên theo loại
       String employeeStatsQuery = "SELECT LoaiNV, COUNT(*) as count FROM NhanVien GROUP BY LoaiNV";
       List<Map<String, Object>> employeeStats = jdbcTemplate.queryForList(employeeStatsQuery);
       Map<String, Object> nhanVienTheoLoai = new HashMap<>();
       labels = new ArrayList<>();
       data = new ArrayList<>();
       for (Map<String, Object> stat : employeeStats) {
           labels.add((String) stat.get("LoaiNV"));
           data.add(((Number) stat.get("count")).intValue());
       }
       nhanVienTheoLoai.put("labels", labels);
       nhanVienTheoLoai.put("data", data);
       stats.put("nhan_vien_theo_loai", nhanVienTheoLoai);

       // Thống kê chuyến bay theo tháng
        String flightsByMonthQuery = """
            SELECT MONTH(NgayDi) as month, COUNT(*) as count 
            FROM LichBay 
            WHERE YEAR(NgayDi) = YEAR(CURRENT_DATE)
            GROUP BY MONTH(NgayDi)
            ORDER BY month
        """;
        List<Map<String, Object>> flightsByMonth = jdbcTemplate.queryForList(flightsByMonthQuery);
        Map<String, Object> chuyenBayTheoThang = new HashMap<>();
        List<String> monthLabels = new ArrayList<>();
        List<Integer> monthData = new ArrayList<>();
        for (Map<String, Object> stat : flightsByMonth) {
            monthLabels.add("Tháng " + stat.get("month"));
            monthData.add(((Number) stat.get("count")).intValue());
        }
        chuyenBayTheoThang.put("labels", monthLabels);
        chuyenBayTheoThang.put("data", monthData);
        stats.put("chuyen_bay_theo_thang", chuyenBayTheoThang);

       return stats;
   }

   // API lấy thông tin lịch bay
   @GetMapping("/api/admin/schedules")
   @ResponseBody
   public Map<String, Object> getSchedules() {
       Map<String, Object> response = new HashMap<>();

       String flightsQuery = "SELECT MaChuyenBay, TenSanBayDi, TenSanBayDen FROM ChuyenBay";
       List<Map<String, Object>> flights = jdbcTemplate.queryForList(flightsQuery);

       String aircraftsQuery = "SELECT SoHieu FROM MayBay";
       List<Map<String, Object>> aircrafts = jdbcTemplate.queryForList(aircraftsQuery);

       String schedulesQuery = """
           SELECT lb.MaChuyenBay, lb.SoHieu, mb.MaLoai, lmb.HangSanXuat, mb.SoGheNgoi,
                  cb.TenSanBayDi, cb.TenSanBayDen, cb.GioDi, cb.GioDen
           FROM LichBay lb
           JOIN MayBay mb ON lb.SoHieu = mb.SoHieu  
           JOIN LoaiMayBay lmb ON mb.MaLoai = lmb.MaLoai
           JOIN ChuyenBay cb ON lb.MaChuyenBay = cb.MaChuyenBay
           """;
       List<Map<String, Object>> schedules = jdbcTemplate.queryForList(schedulesQuery);

       response.put("flights", flights);
       response.put("aircrafts", aircrafts); 
       response.put("schedules", schedules);
       return response;
   }
}
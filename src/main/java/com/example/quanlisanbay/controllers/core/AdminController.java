package com.example.quanlisanbay.controllers.core;

import com.example.quanlisanbay.config.LoginRequired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
   @LoginRequired 
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

    @GetMapping("/api/admin/search-flights")
    @LoginRequired 
    @ResponseBody
    public Map<String, Object> searchFlights(@RequestParam("query") String query) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Kiểm tra xem query có phải là định dạng ngày dd/MM/yyyy không
            if (query.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                // Chuyển đổi định dạng ngày từ dd/MM/yyyy sang yyyy-MM-dd
                String[] dateParts = query.split("/");
                String formattedDate = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
                query = formattedDate;
            }

            String searchQuery = """
                SELECT 
                    MaChuyenBay,
                    TenSanBayDi,
                    TenSanBayDen,
                    GioDi,
                    GioDen
                FROM ChuyenBay
                WHERE 
                    MaChuyenBay LIKE ? OR
                    TenSanBayDi LIKE ? OR
                    TenSanBayDen LIKE ? OR
                    DATE_FORMAT(GioDi, '%H:%i:%s') LIKE ? OR
                    DATE_FORMAT(GioDen, '%H:%i:%s') LIKE ? OR
                    DATE(GioDi) = STR_TO_DATE(?, '%Y-%m-%d') OR
                    DATE(GioDen) = STR_TO_DATE(?, '%Y-%m-%d')
                ORDER BY GioDi DESC
            """;

            String searchPattern = "%" + query + "%";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(
                searchQuery,
                searchPattern, searchPattern, searchPattern, 
                searchPattern, searchPattern,
                query, query
            );

            response.put("success", true);
            response.put("data", results);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }

   // API lấy thông tin máy bay
   @GetMapping("/api/admin/aircraft")
   @LoginRequired 
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

    @GetMapping("/api/admin/search-plane-types")
    @LoginRequired 
    @ResponseBody
    public Map<String, Object> searchPlaneTypes(@RequestParam("query") String query) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String searchQuery = """
                SELECT 
                    MaLoai,
                    HangSanXuat
                FROM LoaiMayBay
                WHERE 
                    MaLoai LIKE ? OR
                    HangSanXuat LIKE ?
                ORDER BY MaLoai
            """;

            String searchPattern = "%" + query + "%";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(
                searchQuery,
                searchPattern, searchPattern
            );

            response.put("success", true);
            response.put("data", results);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }

    @GetMapping("/api/admin/search-aircraft")
    @LoginRequired 
    @ResponseBody
    public Map<String, Object> searchAircraft(@RequestParam("query") String query) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String searchQuery = """
                SELECT mb.SoHieu, mb.MaLoai, lmb.HangSanXuat, mb.SoGheNgoi 
                FROM MayBay mb 
                JOIN LoaiMayBay lmb ON mb.MaLoai = lmb.MaLoai
                WHERE 
                    mb.SoHieu LIKE ? OR
                    mb.MaLoai LIKE ? OR
                    lmb.HangSanXuat LIKE ? OR
                    CAST(mb.SoGheNgoi AS CHAR) LIKE ?
                ORDER BY mb.SoHieu
            """;

            String searchPattern = "%" + query + "%";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(
                searchQuery,
                searchPattern, searchPattern, searchPattern, searchPattern
            );

            response.put("success", true);
            response.put("data", results);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }

   // API lấy thông tin đặt chỗ và khách hàng 
   @GetMapping("/api/admin/bookings")
   @LoginRequired 
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
    @LoginRequired 
    @ResponseBody 
    public Map<String, Object> getFlightDetails(@RequestParam String flight_id) {
        String query = "SELECT GioDi as departure_time, GioDen as arrival_time FROM ChuyenBay WHERE MaChuyenBay = ?";
        return jdbcTemplate.queryForMap(query, flight_id);
    }

    @GetMapping("/api/admin/search-customers")
    @LoginRequired 
    @ResponseBody
    public Map<String, Object> searchCustomers(@RequestParam("query") String query) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String searchQuery = """
                SELECT 
                    kh.MaKH,
                    kh.SDT,
                    kh.HoDem,
                    kh.Ten,
                    kh.DiaChi,
                    COUNT(dc.MaKH) as SoLuotDat
                FROM KhachHang kh
                LEFT JOIN DatCho dc ON kh.MaKH = dc.MaKH
                WHERE 
                    kh.MaKH LIKE ? OR
                    kh.SDT LIKE ? OR
                    kh.HoDem LIKE ? OR
                    kh.Ten LIKE ? OR
                    kh.DiaChi LIKE ?
                GROUP BY kh.MaKH, kh.SDT, kh.HoDem, kh.Ten, kh.DiaChi
                ORDER BY kh.MaKH DESC
            """;

            String searchPattern = "%" + query + "%";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(
                searchQuery,
                searchPattern, searchPattern, searchPattern, 
                searchPattern, searchPattern
            );

            response.put("success", true);
            response.put("data", results);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }

    @GetMapping("/api/admin/search-bookings")
    @LoginRequired 
    @ResponseBody
    public Map<String, Object> searchBookings(@RequestParam("query") String query) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Kiểm tra xem query có phải là định dạng ngày dd/MM/yyyy không
            if (query.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                String[] dateParts = query.split("/");
                query = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
            }

            String searchQuery = """
                SELECT 
                    kh.MaKH,
                    kh.HoDem,
                    kh.Ten,
                    kh.SDT,
                    dc.MaChuyenBay,
                    cb.TenSanBayDi,
                    cb.TenSanBayDen,
                    cb.GioDi,
                    cb.GioDen,
                    dc.NgayDi
                FROM DatCho dc
                JOIN KhachHang kh ON dc.MaKH = kh.MaKH
                JOIN ChuyenBay cb ON dc.MaChuyenBay = cb.MaChuyenBay
                WHERE 
                    kh.MaKH LIKE ? OR
                    CONCAT(kh.HoDem, ' ', kh.Ten) LIKE ? OR
                    kh.SDT LIKE ? OR
                    dc.MaChuyenBay LIKE ? OR
                    cb.TenSanBayDi LIKE ? OR
                    cb.TenSanBayDen LIKE ? OR
                    DATE_FORMAT(cb.GioDi, '%H:%i:%s') LIKE ? OR
                    DATE_FORMAT(cb.GioDen, '%H:%i:%s') LIKE ? OR
                    DATE(cb.GioDi) = STR_TO_DATE(?, '%Y-%m-%d') OR
                    DATE(cb.GioDen) = STR_TO_DATE(?, '%Y-%m-%d')
                ORDER BY cb.GioDi DESC
            """;

            String searchPattern = "%" + query + "%";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(
                searchQuery,
                searchPattern, searchPattern, searchPattern,
                searchPattern, searchPattern, searchPattern,
                searchPattern, searchPattern, query, query
            );

            response.put("success", true);
            response.put("data", results);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }
    
   // API lấy thông tin nhân viên và phân công
   @GetMapping("/api/admin/employees") 
   @LoginRequired 
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

   @GetMapping("/api/admin/search-employees")
   @LoginRequired 
   @ResponseBody
   public Map<String, Object> searchEmployees(@RequestParam("query") String query) {
       Map<String, Object> response = new HashMap<>();
       
       try {
           String searchQuery = """
               SELECT 
                   nv.MaNV,
                   nv.SDT,
                   nv.HoDem,
                   nv.Ten,
                   nv.DiaChi,
                   nv.Luong,
                   nv.LoaiNV
               FROM NhanVien nv
               WHERE 
                   nv.MaNV LIKE ? OR
                   nv.SDT LIKE ? OR
                   nv.HoDem LIKE ? OR
                   nv.Ten LIKE ? OR
                   nv.DiaChi LIKE ? OR
                   nv.LoaiNV LIKE ? OR
                   CAST(nv.Luong AS CHAR) LIKE ?
               ORDER BY nv.MaNV DESC
           """;
   
           String searchPattern = "%" + query + "%";
           List<Map<String, Object>> results = jdbcTemplate.queryForList(
               searchQuery,
               searchPattern, searchPattern, searchPattern, 
               searchPattern, searchPattern, searchPattern,
               searchPattern
           );
   
           response.put("success", true);
           response.put("data", results);
           
       } catch (Exception e) {
           response.put("success", false);
           response.put("error", e.getMessage());
       }
       
       return response;
   }

    @GetMapping("/api/admin/search-assignments")
    @LoginRequired 
    @ResponseBody
    public Map<String, Object> searchAssignments(@RequestParam("query") String query) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Kiểm tra xem query có phải là định dạng ngày dd/MM/yyyy không
            if (query.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                String[] dateParts = query.split("/");
                query = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
            }

            String searchQuery = """
                SELECT 
                    nv.MaNV,
                    CONCAT(nv.HoDem, ' ', nv.Ten) as HoTen,
                    nv.SDT,
                    nv.LoaiNV,
                    pc.MaChuyenBay,
                    cb.TenSanBayDi,
                    cb.TenSanBayDen,
                    cb.GioDi,
                    cb.GioDen,
                    pc.NgayDi
                FROM PhanCong pc
                JOIN NhanVien nv ON pc.MaNV = nv.MaNV
                JOIN ChuyenBay cb ON pc.MaChuyenBay = cb.MaChuyenBay
                WHERE 
                    nv.MaNV LIKE ? OR
                    CONCAT(nv.HoDem, ' ', nv.Ten) LIKE ? OR
                    nv.SDT LIKE ? OR
                    nv.LoaiNV LIKE ? OR
                    pc.MaChuyenBay LIKE ? OR
                    cb.TenSanBayDi LIKE ? OR
                    cb.TenSanBayDen LIKE ? OR
                    DATE_FORMAT(cb.GioDi, '%H:%i:%s') LIKE ? OR
                    DATE_FORMAT(cb.GioDen, '%H:%i:%s') LIKE ? OR
                    DATE(cb.GioDi) = STR_TO_DATE(?, '%Y-%m-%d') OR
                    DATE(cb.GioDen) = STR_TO_DATE(?, '%Y-%m-%d')
                ORDER BY cb.GioDi DESC
            """;

            String searchPattern = "%" + query + "%";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(
                searchQuery,
                searchPattern, searchPattern, searchPattern, searchPattern,
                searchPattern, searchPattern, searchPattern,
                searchPattern, searchPattern, query, query
            );

            response.put("success", true);
            response.put("data", results);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }

   // API lấy thống kê
   @GetMapping("/api/admin/stats")
   @LoginRequired 
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
   @LoginRequired 
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

    @GetMapping("/api/admin/search-schedules")
    @LoginRequired 
    @ResponseBody
    public Map<String, Object> searchSchedules(@RequestParam("query") String query) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Kiểm tra xem query có phải là định dạng ngày dd/MM/yyyy không
            if (query.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                String[] dateParts = query.split("/");
                query = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
            }

            String searchQuery = """
                SELECT 
                    lb.MaChuyenBay,
                    lb.SoHieu,
                    mb.MaLoai,
                    lmb.HangSanXuat,
                    mb.SoGheNgoi,
                    cb.TenSanBayDi,
                    cb.TenSanBayDen,
                    cb.GioDi,
                    cb.GioDen
                FROM LichBay lb
                JOIN MayBay mb ON lb.SoHieu = mb.SoHieu
                JOIN LoaiMayBay lmb ON mb.MaLoai = lmb.MaLoai
                JOIN ChuyenBay cb ON lb.MaChuyenBay = cb.MaChuyenBay
                WHERE 
                    lb.MaChuyenBay LIKE ? OR
                    lb.SoHieu LIKE ? OR
                    lmb.HangSanXuat LIKE ? OR
                    cb.TenSanBayDi LIKE ? OR
                    cb.TenSanBayDen LIKE ? OR
                    DATE_FORMAT(cb.GioDi, '%H:%i:%s') LIKE ? OR
                    DATE_FORMAT(cb.GioDen, '%H:%i:%s') LIKE ? OR
                    DATE(cb.GioDi) = STR_TO_DATE(?, '%Y-%m-%d') OR
                    DATE(cb.GioDen) = STR_TO_DATE(?, '%Y-%m-%d')
                ORDER BY cb.GioDi DESC
            """;

            String searchPattern = "%" + query + "%";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(
                searchQuery,
                searchPattern, searchPattern, searchPattern, 
                searchPattern, searchPattern, searchPattern,
                searchPattern, query, query
            );

            response.put("success", true);
            response.put("data", results);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }
}
package com.example.quanlisanbay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
public class AuthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/api/auth")
    @ResponseBody
    public String auth(@RequestParam String username, @RequestParam String password, HttpSession session) {
        String sql = "SELECT COUNT(*) FROM Admins WHERE UserName = ? AND PasswordHash = ?";
        String hashedPassword = hashPassword(password);
        
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, hashedPassword);
        
        if (count != null && count > 0) {
            session.setAttribute("username", username);
            return "Đăng nhập thành công";
        } else {
            return "Tài khoản hoặc mật khẩu không đúng";
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi khi mã hóa mật khẩu", e);
        }
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.removeAttribute("username");
        return new RedirectView("/san_bay");
    }
}
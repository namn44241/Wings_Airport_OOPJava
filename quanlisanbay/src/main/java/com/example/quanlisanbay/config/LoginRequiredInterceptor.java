package com.example.quanlisanbay.config;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);
            if (loginRequired != null) {
                // Bỏ qua kiểm tra cho trang đăng nhập
                if (request.getRequestURI().equals("/login")) {
                    return true;
                }
                // Kiểm tra session username
                HttpSession session = request.getSession(false); // Lấy session hiện tại nếu có
                if (session == null || session.getAttribute("username") == null) {
                    response.sendRedirect("/login");
                    return false;
                }
                System.out.println("Session username in interceptor: " + session.getAttribute("username"));
            }
        }
        return true;
    }

}

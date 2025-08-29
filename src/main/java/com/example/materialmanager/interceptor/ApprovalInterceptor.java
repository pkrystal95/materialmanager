package com.example.materialmanager.interceptor;

import com.example.materialmanager.domain.Role;
import com.example.materialmanager.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApprovalInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        
        // 인증 관련, 정적 자원, API, 관리 페이지는 제외
        if (requestURI.startsWith("/auth/") || 
            requestURI.startsWith("/css/") || 
            requestURI.startsWith("/js/") || 
            requestURI.startsWith("/images/") ||
            requestURI.startsWith("/users/") ||  // 사용자 관리 페이지 제외
            requestURI.equals("/")) {
            return true;
        }
        
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("/auth/login");
            return false;
        }
        
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.sendRedirect("/auth/login");
            return false;
        }
        
        // 관리자는 승인 없이도 접근 가능
        if (loginUser.getRole() == Role.ADMIN) {
            return true;
        }
        
        return true;
    }
}
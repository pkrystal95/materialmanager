package com.example.materialmanager.common;

import com.example.materialmanager.domain.Role;
import com.example.materialmanager.domain.User;
import jakarta.servlet.http.HttpSession;

public final class SecurityUtils {
    
    private SecurityUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 현재 로그인한 사용자를 반환합니다.
     */
    public static User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute(Constants.SESSION_LOGIN_USER);
    }
    
    /**
     * 사용자가 로그인되어 있는지 확인합니다.
     */
    public static boolean isLoggedIn(HttpSession session) {
        return getCurrentUser(session) != null;
    }
    
    /**
     * 사용자가 관리자 권한을 가지고 있는지 확인합니다.
     */
    public static boolean isAdmin(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null && user.getRole() == Role.ADMIN;
    }
    
    /**
     * 사용자가 강사 또는 관리자 권한을 가지고 있는지 확인합니다.
     */
    public static boolean isTeacherOrAdmin(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null && (user.getRole() == Role.TEACHER || user.getRole() == Role.ADMIN);
    }
    
    /**
     * 사용자가 승인되었는지 확인합니다.
     */
    public static boolean isApproved(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null && (user.getRole() == Role.ADMIN || user.isApproved());
    }
    
    /**
     * 사용자가 해당 리소스에 접근 권한이 있는지 확인합니다.
     */
    public static boolean hasAccessTo(HttpSession session, Long userId) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return false;
        }
        
        // 관리자는 모든 리소스에 접근 가능
        if (currentUser.getRole() == Role.ADMIN) {
            return true;
        }
        
        // 본인의 리소스에만 접근 가능
        return currentUser.getId().equals(userId);
    }
}
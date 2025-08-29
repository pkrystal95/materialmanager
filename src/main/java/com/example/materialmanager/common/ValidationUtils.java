package com.example.materialmanager.common;

public final class ValidationUtils {
    
    private ValidationUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 문자열이 null이거나 비어있거나 공백만 있는지 확인합니다.
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * 문자열이 null이 아니고 비어있지 않고 공백만 있지 않은지 확인합니다.
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    
    /**
     * 검색어가 유효한지 확인합니다. (최소 길이 확인)
     */
    public static boolean isValidSearchQuery(String query) {
        return isNotBlank(query) && query.trim().length() >= Constants.MIN_SEARCH_LENGTH;
    }
    
    /**
     * 이메일 형식이 유효한지 확인합니다. (간단한 검증)
     */
    public static boolean isValidEmail(String email) {
        if (isBlank(email)) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * 페이지 번호가 유효한지 확인합니다.
     */
    public static boolean isValidPageNumber(Integer page) {
        return page != null && page >= 0;
    }
    
    /**
     * 페이지 크기가 유효한지 확인합니다.
     */
    public static boolean isValidPageSize(Integer size) {
        return size != null && size > 0 && size <= 100;
    }
}
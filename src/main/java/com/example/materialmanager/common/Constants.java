package com.example.materialmanager.common;

public final class Constants {
    
    // 세션 키
    public static final String SESSION_LOGIN_USER = "loginUser";
    
    // 정렬 관련
    public static final String SORT_CREATED_AT = "createdAt";
    public static final String SORT_NAME = "name";
    public static final String ORDER_ASC = "asc";
    public static final String ORDER_DESC = "desc";
    
    // 상태 관련
    public static final String STATUS_ALL = "all";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_APPROVED = "approved";
    
    // 기본 페이지 크기
    public static final int DEFAULT_PAGE_SIZE = 20;
    
    // 검색 관련
    public static final int MIN_SEARCH_LENGTH = 2;
    
    // 에러 메시지
    public static final String ERROR_USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";
    public static final String ERROR_LECTURE_NOT_FOUND = "강의를 찾을 수 없습니다.";
    public static final String ERROR_MATERIAL_NOT_FOUND = "자료를 찾을 수 없습니다.";
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "이미 존재하는 이메일입니다.";
    public static final String ERROR_ACCESS_DENIED = "접근 권한이 없습니다.";
    public static final String ERROR_NOT_APPROVED = "승인되지 않은 사용자입니다.";
    
    // 성공 메시지
    public static final String SUCCESS_USER_CREATED = "사용자가 성공적으로 등록되었습니다.";
    public static final String SUCCESS_USER_UPDATED = "사용자 정보가 성공적으로 수정되었습니다.";
    public static final String SUCCESS_USER_APPROVED = "사용자가 성공적으로 승인되었습니다.";
    public static final String SUCCESS_USER_DELETED = "사용자가 성공적으로 삭제되었습니다.";
    
    // 리다이렉트 경로
    public static final String REDIRECT_LOGIN = "redirect:/auth/login";
    public static final String REDIRECT_USERS = "redirect:/users";
    public static final String REDIRECT_LECTURES = "redirect:/lectures";
    public static final String REDIRECT_MATERIALS = "redirect:/materials";
    public static final String REDIRECT_HOME = "redirect:/";
    
    // 추가 뷰 이름
    public static final String VIEW_SEARCH_RESULTS = "search/results";
    public static final String VIEW_FAVORITE_MY = "favorite/my";
    public static final String VIEW_AUTH_LOGIN = "auth/login";
    public static final String VIEW_AUTH_SIGNUP = "auth/signup";
    public static final String VIEW_INDEX = "index";
    
    // 뷰 이름
    public static final String VIEW_USERS_LIST = "users/list";
    public static final String VIEW_USERS_FORM = "users/form";
    public static final String VIEW_USERS_EDIT = "users/edit";
    public static final String VIEW_USERS_DETAIL = "users/detail";
    public static final String VIEW_LECTURES_LIST = "lecture/list";
    public static final String VIEW_LECTURES_FORM = "lecture/form";
    public static final String VIEW_MATERIALS_LIST = "material/list";
    public static final String VIEW_MATERIALS_FORM = "material/form";
    
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
package com.example.materialmanager.common;

import com.example.materialmanager.domain.User;
import java.util.Comparator;
import java.util.List;

public final class SortingUtils {
    
    private SortingUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 사용자 목록을 정렬합니다.
     */
    public static List<User> sortUsers(List<User> users, String sortBy, String order) {
        if (users == null || users.isEmpty()) {
            return users;
        }
        
        Comparator<User> comparator = getUserComparator(sortBy);
        
        if (Constants.ORDER_DESC.equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        
        return users.stream().sorted(comparator).toList();
    }
    
    /**
     * 사용자 정렬 비교자를 반환합니다.
     */
    private static Comparator<User> getUserComparator(String sortBy) {
        return switch (sortBy) {
            case Constants.SORT_NAME -> 
                Comparator.comparing(user -> user.getName() == null ? "" : user.getName().toLowerCase());
            case "email" ->
                Comparator.comparing(user -> user.getEmail() == null ? "" : user.getEmail().toLowerCase());
            case "role" ->
                Comparator.comparing(user -> user.getRole() == null ? "" : user.getRole().name());
            default -> // createdAt
                Comparator.comparing(User::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
        };
    }
    
    /**
     * 정렬 방향이 유효한지 확인합니다.
     */
    public static boolean isValidOrder(String order) {
        return Constants.ORDER_ASC.equalsIgnoreCase(order) || 
               Constants.ORDER_DESC.equalsIgnoreCase(order);
    }
    
    /**
     * 기본 정렬 방향을 반환합니다.
     */
    public static String getDefaultOrder(String order) {
        return isValidOrder(order) ? order : Constants.ORDER_DESC;
    }
}
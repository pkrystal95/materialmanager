package com.example.materialmanager.service;

import com.example.materialmanager.common.Constants;
import com.example.materialmanager.common.SortingUtils;
import com.example.materialmanager.common.ValidationUtils;
import com.example.materialmanager.domain.Role;
import com.example.materialmanager.domain.User;
import com.example.materialmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // 생성자 주입
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 전체 사용자 조회
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * 사용자 검색 (이름 또는 이메일로)
     */
    public List<User> searchUsers(String query) {
        if (ValidationUtils.isBlank(query)) {
            return findAll();
        }
        return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query.trim(), query.trim());
    }
    
    /**
     * 상태별 사용자 조회 및 검색
     */
    public List<User> findUsersByStatusAndQuery(String status, String query, String sortBy, String order) {
        List<User> users;
        
        // 상태별 필터링
        if (Constants.STATUS_PENDING.equalsIgnoreCase(status)) {
            users = findByApproved(false);
        } else if (Constants.STATUS_APPROVED.equalsIgnoreCase(status)) {
            users = findByApproved(true);
        } else {
            users = findAll();
        }
        
        // 검색어 적용
        if (ValidationUtils.isNotBlank(query)) {
            final String searchQuery = query.trim().toLowerCase();
            users = users.stream()
                    .filter(user -> (user.getName() != null && user.getName().toLowerCase().contains(searchQuery))
                            || (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchQuery)))
                    .toList();
        }
        
        // 정렬 적용
        return SortingUtils.sortUsers(users, 
                sortBy != null ? sortBy : Constants.SORT_CREATED_AT, 
                SortingUtils.getDefaultOrder(order));
    }

    public List<User> findByApproved(boolean approved) {
        return userRepository.findByApproved(approved);
    }

    public long countApproved(boolean approved) {
        return userRepository.countByApproved(approved);
    }

    // id로 사용자 조회
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // 이메일로 사용자 조회 (로그인용)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 사용자 저장 (회원가입)
     */
    public void save(User user) {
        validateUser(user);
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(Constants.ERROR_EMAIL_ALREADY_EXISTS);
        }
        
        setupUserApprovalStatus(user);
        userRepository.save(user);
    }
    
    /**
     * 사용자 승인 상태 설정
     */
    private void setupUserApprovalStatus(User user) {
        if (user.getRole() == Role.ADMIN) {
            user.setApproved(true);
            user.setApprovedAt(LocalDateTime.now());
        } else {
            user.setApproved(false);
        }
    }
    
    /**
     * 사용자 유효성 검증
     */
    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }
        if (ValidationUtils.isBlank(user.getName())) {
            throw new IllegalArgumentException("이름을 입력해주세요.");
        }
        if (!ValidationUtils.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("올바른 이메일 형식을 입력해주세요.");
        }
        if (user.getRole() == null) {
            throw new IllegalArgumentException("역할을 선택해주세요.");
        }
    }

    /**
     * 사용자 수정
     */
    public void update(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(Constants.ERROR_USER_NOT_FOUND));
        
        validateUser(updatedUser);
        
        // 이메일 중복 체크 (자신의 이메일이 아닌 경우만)
        if (!existingUser.getEmail().equals(updatedUser.getEmail()) && 
            userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new IllegalArgumentException(Constants.ERROR_EMAIL_ALREADY_EXISTS);
        }
        
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());
        
        userRepository.save(existingUser);
    }

    /**
     * 사용자 삭제
     */
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException(Constants.ERROR_USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    /**
     * 승인 처리
     */
    public void approve(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(Constants.ERROR_USER_NOT_FOUND));
        
        if (user.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("관리자는 승인 처리할 수 없습니다.");
        }
        
        if (user.isApproved()) {
            throw new IllegalArgumentException("이미 승인된 사용자입니다.");
        }
        
        user.setApproved(true);
        user.setApprovedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    /**
     * 사용자 통계 조회
     */
    public UserStats getUserStats() {
        long totalUsers = userRepository.count();
        long approvedUsers = countApproved(true);
        long pendingUsers = countApproved(false);
        
        return new UserStats(totalUsers, approvedUsers, pendingUsers);
    }
    
    /**
     * 사용자 통계 DTO
     */
    public static class UserStats {
        private final long total;
        private final long approved;
        private final long pending;
        
        public UserStats(long total, long approved, long pending) {
            this.total = total;
            this.approved = approved;
            this.pending = pending;
        }
        
        public long getTotal() { return total; }
        public long getApproved() { return approved; }
        public long getPending() { return pending; }
    }
}

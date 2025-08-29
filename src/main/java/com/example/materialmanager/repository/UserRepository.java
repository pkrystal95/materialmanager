package com.example.materialmanager.repository;

import com.example.materialmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);

    // 승인/미승인 목록 및 카운트
    List<User> findByApproved(boolean approved);

    long countByApproved(boolean approved);

    // 검색 (이름/이메일)
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);

}

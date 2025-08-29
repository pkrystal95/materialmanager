package com.example.materialmanager.repository;

import com.example.materialmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);

}

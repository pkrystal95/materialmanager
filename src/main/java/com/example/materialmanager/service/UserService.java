package com.example.materialmanager.service;

import com.example.materialmanager.domain.User;
import com.example.materialmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

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

    // id로 사용자 조회
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // 이메일로 사용자 조회 (로그인용)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 사용자 저장 (회원가입)
    public User save(User user) {
        return userRepository.save(user);
    }

    // 사용자 삭제
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}

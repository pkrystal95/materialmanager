package com.example.materialmanager.controller;

import com.example.materialmanager.domain.User;
import com.example.materialmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService; // UserService 선언

    // 생성자 주입
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User()); // User 객체 모델에 추가
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {

        User user = userService.findByEmail(email).orElse(null);

        if (user == null || !user.getPassword().equals(password)) {
            // 실패 메시지 모델에 전달
            model.addAttribute("loginError", "이메일 또는 비밀번호가 잘못되었습니다.");
            return "auth/login";
        }

        // 로그인 성공 메시지
        model.addAttribute("loginSuccess", "로그인 성공!");
        model.addAttribute("loginUser", user); // 간단 세션 대신 모델 전달
        return "index"; // 로그인 성공 시 메인 화면
    }

}

package com.example.materialmanager.controller;

import com.example.materialmanager.domain.User;
import com.example.materialmanager.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
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
                        HttpSession session,
                        Model model) {

        User user = userService.findByEmail(email).orElse(null);

        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("loginError", "이메일 또는 비밀번호가 잘못되었습니다.");
            return "auth/login";
        }

        // 세션에 로그인 사용자 저장
        session.setAttribute("loginUser", user);
        model.addAttribute("loginSuccess", "로그인 성공!");
        return "redirect:/"; // 로그인 성공 시 메인 화면
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 초기화
        return "redirect:/auth/login";
    }
}

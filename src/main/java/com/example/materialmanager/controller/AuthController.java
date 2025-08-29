package com.example.materialmanager.controller;

import com.example.materialmanager.domain.Role;
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

    public AuthController(UserService userService) { this.userService = userService; }

    @GetMapping("/login")
    public String login() { return "auth/login"; }

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

        // 승인되지 않은 사용자(ADMIN 제외)는 로그인 불가
        if (user.getRole() != Role.ADMIN && !user.isApproved()) {
            model.addAttribute("loginError", "관리자 승인이 필요합니다. 승인 후 로그인 가능합니다.");
            return "auth/login";
        }

        session.setAttribute("loginUser", user);
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values()); // Role 목록 전달
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user, Model model) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("signupError", "이미 사용 중인 이메일입니다.");
            return "auth/signup";
        }

        userService.save(user);
        // 승인 대기 안내를 위해 플래그 전달
        boolean pending = user.getRole() != Role.ADMIN;
        return pending ? "redirect:/auth/login?pending=true" : "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}

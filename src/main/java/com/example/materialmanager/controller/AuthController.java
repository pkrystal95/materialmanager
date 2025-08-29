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
    public String signupSubmit(@ModelAttribute User user) {
        if (user.getRole() == null) user.setRole(Role.STUDENT); // 기본값
        userService.save(user);
        return "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}

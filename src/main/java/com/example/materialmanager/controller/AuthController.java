package com.example.materialmanager.controller;

import com.example.materialmanager.common.Constants;
import com.example.materialmanager.common.SecurityUtils;
import com.example.materialmanager.common.ValidationUtils;
import com.example.materialmanager.domain.Role;
import com.example.materialmanager.domain.User;
import com.example.materialmanager.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 로그인 폼 페이지
     */
    @GetMapping("/login")
    public String login(HttpSession session) {
        // 이미 로그인되어 있다면 홈으로 리다이렉트
        if (SecurityUtils.isLoggedIn(session)) {
            return Constants.REDIRECT_HOME;
        }
        return Constants.VIEW_AUTH_LOGIN;
    }

    /**
     * 로그인 처리
     */
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        // 입력 유효성 검증
        if (ValidationUtils.isBlank(email) || ValidationUtils.isBlank(password)) {
            model.addAttribute("loginError", "이메일과 비밀번호를 입력해주세요.");
            return Constants.VIEW_AUTH_LOGIN;
        }

        // 사용자 인증
        User user = userService.findByEmail(email.trim()).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("loginError", "이메일 또는 비밀번호가 잘못되었습니다.");
            return Constants.VIEW_AUTH_LOGIN;
        }

        // 승인 상태 확인
        if (user.getRole() != Role.ADMIN && !user.isApproved()) {
            model.addAttribute("loginError", Constants.ERROR_NOT_APPROVED);
            return Constants.VIEW_AUTH_LOGIN;
        }

        session.setAttribute(Constants.SESSION_LOGIN_USER, user);
        return Constants.REDIRECT_HOME;
    }

    /**
     * 회원가입 폼 페이지
     */
    @GetMapping("/signup")
    public String signupForm(HttpSession session, Model model) {
        // 이미 로그인되어 있다면 홈으로 리다이렉트
        if (SecurityUtils.isLoggedIn(session)) {
            return Constants.REDIRECT_HOME;
        }

        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return Constants.VIEW_AUTH_SIGNUP;
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user, HttpSession session,
                             Model model, RedirectAttributes redirectAttributes) {
        // 이미 로그인되어 있다면 홈으로 리다이렉트
        if (SecurityUtils.isLoggedIn(session)) {
            return Constants.REDIRECT_HOME;
        }

        try {
            userService.save(user);

            // 승인 대기 메시지 설정
            if (user.getRole() != Role.ADMIN) {
                redirectAttributes.addFlashAttribute("pendingMessage",
                    "회원가입이 완료되었습니다. 관리자 승인 후 로그인할 수 있습니다.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage",
                    "회원가입이 완료되었습니다. 로그인해주세요.");
            }

            return Constants.REDIRECT_LOGIN;

        } catch (IllegalArgumentException e) {
            model.addAttribute("signupError", e.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return Constants.VIEW_AUTH_SIGNUP;
        }
    }

    /**
     * 로그아웃 처리
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        if (SecurityUtils.isLoggedIn(session)) {
            session.invalidate();
            redirectAttributes.addFlashAttribute("logoutMessage", "로그아웃되었습니다.");
        }
        return Constants.REDIRECT_LOGIN;
    }
}

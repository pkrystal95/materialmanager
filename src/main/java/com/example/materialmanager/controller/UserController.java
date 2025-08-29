package com.example.materialmanager.controller;

import com.example.materialmanager.domain.Role;
import com.example.materialmanager.domain.User;
import com.example.materialmanager.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private boolean isAdmin(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        return loginUser != null && loginUser.getRole() == Role.ADMIN;
    }


    // 회원 목록 조회 (관리자 전용)
    @GetMapping
    public String list(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }
        model.addAttribute("users", userService.findAll());
        return "users/list";
    }

    // 사용자 상세보기
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", user);
        return "users/detail";
    }

    // 사용자 등록 폼
    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }

    // 사용자 등록 처리
    @PostMapping
    public String save(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/users";
    }

    // 회원 수정 폼
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "users/edit";
    }

    // 회원 수정 처리
    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Long id, @ModelAttribute User updatedUser, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }
        userService.update(id, updatedUser);
        return "redirect:/user";
    }


    // 회원 삭제 - 관리자만 가능
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }
        userService.delete(id);
        return "redirect:/users";
    }
}

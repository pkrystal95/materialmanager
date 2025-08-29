package com.example.materialmanager.controller;

import com.example.materialmanager.domain.User;
import com.example.materialmanager.service.UserService;
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

    // 모든 사용자 리스트
    @GetMapping
    public String list(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    // 사용자 상세보기
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", user);
        return "user/detail";
    }

    // 사용자 등록 폼
    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "user/form";
    }

    // 사용자 등록 처리
    @PostMapping
    public String save(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/users";
    }
}

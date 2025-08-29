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
    public String list(HttpSession session, Model model,
                       @RequestParam(name = "status", required = false) String status,
                       @RequestParam(name = "q", required = false) String q,
                       @RequestParam(name = "sort", required = false, defaultValue = "createdAt") String sort,
                       @RequestParam(name = "order", required = false, defaultValue = "desc") String order) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }
        java.util.List<User> users;
        if ("pending".equalsIgnoreCase(status)) {
            users = userService.findByApproved(false);
        } else if ("approved".equalsIgnoreCase(status)) {
            users = userService.findByApproved(true);
        } else {
            users = userService.findAll();
        }

        // 검색 적용
        if (q != null && !q.isBlank()) {
            users = users.stream()
                    .filter(u -> (u.getName() != null && u.getName().toLowerCase().contains(q.toLowerCase()))
                            || (u.getEmail() != null && u.getEmail().toLowerCase().contains(q.toLowerCase())))
                    .toList();
        }

        // 정렬 적용
        java.util.Comparator<User> comparator;
        if ("name".equalsIgnoreCase(sort)) {
            comparator = java.util.Comparator.comparing(u -> u.getName() == null ? "" : u.getName().toLowerCase());
        } else { // createdAt
            comparator = java.util.Comparator.comparing(User::getCreatedAt);
        }
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        users = users.stream().sorted(comparator).toList();

        model.addAttribute("users", users);
        model.addAttribute("countAll", userService.findAll().size());
        model.addAttribute("countApproved", userService.countApproved(true));
        model.addAttribute("countPending", userService.countApproved(false));
        model.addAttribute("activeStatus", status == null ? "all" : status);
        model.addAttribute("q", q);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
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
        return "redirect:/users";
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

    // 승인 처리 (관리자 전용)
    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }
        userService.approve(id);
        return "redirect:/users";
    }
}

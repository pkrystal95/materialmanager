package com.example.materialmanager.controller;

import com.example.materialmanager.common.Constants;
import com.example.materialmanager.common.SecurityUtils;
import com.example.materialmanager.domain.Role;
import com.example.materialmanager.domain.User;
import com.example.materialmanager.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    /**
     * 회원 목록 조회 (관리자 전용)
     */
    @GetMapping
    public String list(HttpSession session, Model model,
                       @RequestParam(name = "status", required = false) String status,
                       @RequestParam(name = "q", required = false) String query,
                       @RequestParam(name = "sort", required = false, defaultValue = Constants.SORT_CREATED_AT) String sort,
                       @RequestParam(name = "order", required = false, defaultValue = Constants.ORDER_DESC) String order) {
        
        if (!SecurityUtils.isAdmin(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        // 사용자 목록 조회 (필터링, 검색, 정렬 포함)
        List<User> users = userService.findUsersByStatusAndQuery(status, query, sort, order);
        
        // 통계 정보 조회
        UserService.UserStats stats = userService.getUserStats();
        
        model.addAttribute("users", users);
        model.addAttribute("countAll", stats.getTotal());
        model.addAttribute("countApproved", stats.getApproved());
        model.addAttribute("countPending", stats.getPending());
        model.addAttribute("activeStatus", status == null ? Constants.STATUS_ALL : status);
        model.addAttribute("q", query);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        
        return Constants.VIEW_USERS_LIST;
    }

    /**
     * 사용자 상세보기
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        if (!SecurityUtils.isAdmin(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(Constants.ERROR_USER_NOT_FOUND));
        model.addAttribute("user", user);
        return Constants.VIEW_USERS_DETAIL;
    }

    /**
     * 사용자 등록 폼
     */
    @GetMapping("/form")
    public String form(HttpSession session, Model model) {
        if (!SecurityUtils.isAdmin(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return Constants.VIEW_USERS_FORM;
    }

    /**
     * 사용자 등록 처리
     */
    @PostMapping
    public String save(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!SecurityUtils.isAdmin(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        try {
            userService.save(user);
            redirectAttributes.addFlashAttribute("successMessage", Constants.SUCCESS_USER_CREATED);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return Constants.REDIRECT_USERS;
    }

    /**
     * 회원 수정 폼
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!SecurityUtils.isAdmin(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(Constants.ERROR_USER_NOT_FOUND));
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return Constants.VIEW_USERS_EDIT;
    }

    /**
     * 회원 수정 처리
     */
    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Long id, @ModelAttribute User updatedUser, 
                           HttpSession session, RedirectAttributes redirectAttributes) {
        if (!SecurityUtils.isAdmin(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        try {
            userService.update(id, updatedUser);
            redirectAttributes.addFlashAttribute("successMessage", Constants.SUCCESS_USER_UPDATED);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return Constants.REDIRECT_USERS;
    }


    /**
     * 회원 삭제 - 관리자만 가능
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!SecurityUtils.isAdmin(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", Constants.SUCCESS_USER_DELETED);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return Constants.REDIRECT_USERS;
    }

    /**
     * 승인 처리 (관리자 전용)
     */
    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!SecurityUtils.isAdmin(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        try {
            userService.approve(id);
            redirectAttributes.addFlashAttribute("successMessage", Constants.SUCCESS_USER_APPROVED);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return Constants.REDIRECT_USERS;
    }
}

package com.example.materialmanager.controller;

import com.example.materialmanager.domain.User;
import com.example.materialmanager.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorite/my")
    public String myFavorites(Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("favorites", favoriteService.findByUserId(loginUser.getId()));
        return "favorite/my";
    }
    
    @PostMapping("/favorites/toggle/{materialId}")
    public String toggleFavorite(@PathVariable Long materialId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/auth/login";
        }
        
        favoriteService.toggleFavorite(loginUser.getId(), materialId);
        return "redirect:/materials";
    }
}

package com.example.materialmanager.controller;

import com.example.materialmanager.common.Constants;
import com.example.materialmanager.common.SecurityUtils;
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
        if (!SecurityUtils.isLoggedIn(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        User loginUser = SecurityUtils.getCurrentUser(session);
        model.addAttribute("favorites", favoriteService.findByUserId(loginUser.getId()));
        return Constants.VIEW_FAVORITE_MY;
    }
    
    @PostMapping("/favorites/toggle/{materialId}")
    public String toggleFavorite(@PathVariable Long materialId, HttpSession session) {
        if (!SecurityUtils.isLoggedIn(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        User loginUser = SecurityUtils.getCurrentUser(session);
        favoriteService.toggleFavorite(loginUser.getId(), materialId);
        return Constants.REDIRECT_MATERIALS;
    }
}

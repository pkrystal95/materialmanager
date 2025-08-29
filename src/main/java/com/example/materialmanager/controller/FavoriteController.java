package com.example.materialmanager.controller;

import com.example.materialmanager.service.FavoriteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/my")
    public String myFavorites(Model model) {
        // 예시: userId를 임시로 1L로 지정
        model.addAttribute("favorites", favoriteService.findByUserId(1L));
        return "favorite/my";
    }
}

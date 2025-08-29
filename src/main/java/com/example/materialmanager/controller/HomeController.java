package com.example.materialmanager.controller;

import com.example.materialmanager.common.Constants;
import com.example.materialmanager.common.SecurityUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    /**
     * 홈페이지
     */
    @GetMapping("/")
    public String home(HttpSession session) {
        if (!SecurityUtils.isLoggedIn(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        return Constants.VIEW_INDEX;
    }
}
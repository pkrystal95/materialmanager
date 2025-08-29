package com.example.materialmanager.controller;

import com.example.materialmanager.domain.Lecture;
import com.example.materialmanager.domain.Material;
import com.example.materialmanager.service.LectureService;
import com.example.materialmanager.service.MaterialService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final MaterialService materialService;
    private final LectureService lectureService;

    public SearchController(MaterialService materialService, LectureService lectureService) {
        this.materialService = materialService;
        this.lectureService = lectureService;
    }

    @GetMapping("/search")
    public String globalSearch(@RequestParam("q") String query, HttpSession session, Model model) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/auth/login";
        }

        if (query == null || query.trim().isEmpty()) {
            return "redirect:/";
        }

        // Search in materials
        List<Material> materials = materialService.searchMaterials(query, null, null);
        
        // Search in lectures
        List<Lecture> lectures = lectureService.searchLectures(query, query, null);

        model.addAttribute("query", query);
        model.addAttribute("materials", materials);
        model.addAttribute("lectures", lectures);
        model.addAttribute("totalResults", materials.size() + lectures.size());

        return "search/results";
    }
}
package com.example.materialmanager.controller;

import com.example.materialmanager.domain.Lecture;
import com.example.materialmanager.domain.Material;
import com.example.materialmanager.service.LectureService;
import com.example.materialmanager.service.MaterialService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        // 로그인 여부 확인
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("lectures", lectureService.findAll());
        return "lecture/list";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("lecture", new Lecture()); // th:object 용
        return "lecture/form";
    }

    @PostMapping("/form")
    public String submit(@ModelAttribute Lecture lecture) {
        lectureService.save(lecture);
        return "redirect:/lectures";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        lectureService.delete(id);
        return "redirect:/lectures";
    }
}

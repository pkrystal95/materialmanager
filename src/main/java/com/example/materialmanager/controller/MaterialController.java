package com.example.materialmanager.controller;

import com.example.materialmanager.domain.Material;
import com.example.materialmanager.service.LectureService;
import com.example.materialmanager.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;
    private final LectureService lectureService;

    public MaterialController(MaterialService materialService, LectureService lectureService) {
        this.materialService = materialService;
        this.lectureService = lectureService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Long lectureId, Model model) {
        if (lectureId != null) {
            model.addAttribute("materials", materialService.findByLectureId(lectureId));
        } else {
            model.addAttribute("materials", materialService.findAll());
        }
        return "material/list";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("material", new Material());           // th:object용
        model.addAttribute("lectures", lectureService.findAll()); // select box용
        return "material/form";
    }

    @PostMapping("/form")
    public String submit(@ModelAttribute Material material) {
        // select box에서 넘어온 lecture.id로 실제 Lecture 객체 설정
        Long lectureId = material.getLecture().getId();
        material.setLecture(
                lectureService.findById(lectureId)
                        .orElseThrow(() -> new IllegalArgumentException("Lecture not found"))
        );
        materialService.save(material);
        return "redirect:/materials?lectureId=" + lectureId;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        materialService.delete(id);
        return "redirect:/materials";
    }
}

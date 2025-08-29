package com.example.materialmanager.controller;

import com.example.materialmanager.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public String list(@RequestParam Long lectureId, Model model) {
        model.addAttribute("materials", materialService.findByLectureId(lectureId));
        return "material/list";
    }
}

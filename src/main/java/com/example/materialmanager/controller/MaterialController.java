package com.example.materialmanager.controller;

import com.example.materialmanager.domain.Material;
import com.example.materialmanager.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
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
        model.addAttribute("material", new Material());
        return "material/form";
    }

    @PostMapping("/form")
    public String submit(@ModelAttribute Material material) {
        materialService.save(material);
        return "redirect:/materials?lectureId=" + material.getLecture().getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        materialService.delete(id);
        return "redirect:/materials";
    }
}

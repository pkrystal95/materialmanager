package com.example.materialmanager.controller;

import com.example.materialmanager.domain.Material;
import com.example.materialmanager.domain.MaterialType;
import com.example.materialmanager.service.LectureService;
import com.example.materialmanager.service.MaterialService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String list(@RequestParam(required = false) Long lectureId,
                      @RequestParam(required = false) String title,
                      @RequestParam(required = false) String type,
                      HttpSession session, Model model) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/auth/login";
        }

        // Search functionality
        if (title != null || type != null || lectureId != null) {
            model.addAttribute("materials", materialService.searchMaterials(title, type, lectureId));
        } else {
            model.addAttribute("materials", materialService.findAll());
        }
        
        // Add lectures for dropdown
        model.addAttribute("lectures", lectureService.findAll());
        return "material/list";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("material", new Material());
        model.addAttribute("lectures", lectureService.findAll());
        model.addAttribute("types", MaterialType.values());
        return "material/form";
    }

    @PostMapping("/form")
    public String submit(@Valid @ModelAttribute Material material,
                         BindingResult bindingResult,
                         Model model) {

        model.addAttribute("lectures", lectureService.findAll());
        model.addAttribute("types", MaterialType.values());

        if (bindingResult.hasErrors()) {
            return "material/form";
        }

        if (material.getLecture() == null || material.getLecture().getId() == null) {
            bindingResult.rejectValue("lecture", "invalid", "강의를 선택해주세요.");
            return "material/form";
        }

        lectureService.findById(material.getLecture().getId())
                .ifPresentOrElse(
                        lecture -> material.setLecture(lecture),
                        () -> bindingResult.rejectValue("lecture", "invalid", "선택한 강의가 존재하지 않습니다.")
                );

        if (bindingResult.hasErrors()) {
            return "material/form";
        }

        materialService.save(material);
        return "redirect:/materials?lectureId=" + material.getLecture().getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        materialService.delete(id);
        return "redirect:/materials";
    }
}

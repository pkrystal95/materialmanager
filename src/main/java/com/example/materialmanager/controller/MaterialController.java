package com.example.materialmanager.controller;

import com.example.materialmanager.domain.Material;
import com.example.materialmanager.domain.MaterialType;
import com.example.materialmanager.domain.User;
import com.example.materialmanager.service.FavoriteService;
import com.example.materialmanager.service.LectureService;
import com.example.materialmanager.service.MaterialService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;
    private final LectureService lectureService;
    private final FavoriteService favoriteService;

    public MaterialController(MaterialService materialService, 
                             LectureService lectureService,
                             FavoriteService favoriteService) {
        this.materialService = materialService;
        this.lectureService = lectureService;
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Long lectureId,
                      @RequestParam(required = false) String title,
                      @RequestParam(required = false) String type,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "12") int size,
                      @RequestParam(defaultValue = "id") String sort,
                      @RequestParam(defaultValue = "desc") String direction,
                      HttpSession session, Model model) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/auth/login";
        }

        // 정렬 설정
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortField = getSortField(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));

        // Search functionality - check for non-empty parameters
        boolean hasSearchParams = (title != null && !title.trim().isEmpty()) || 
                                 (type != null && !type.trim().isEmpty()) || 
                                 (lectureId != null);
        
        Page<Material> materialPage;
        if (hasSearchParams) {
            materialPage = materialService.searchMaterialsPaged(title, type, lectureId, pageable);
        } else {
            materialPage = materialService.findAllPaged(pageable);
        }
        
        model.addAttribute("materials", materialPage.getContent());
        model.addAttribute("page", materialPage);
        model.addAttribute("lectures", lectureService.findAll());
        
        // 즐겨찾기 상태를 한 번에 조회하여 Map으로 전달
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            List<Long> materialIds = materialPage.getContent().stream()
                .map(Material::getId)
                .toList();
            Map<Long, Boolean> favoriteMap = favoriteService.getFavoriteStatusMap(loginUser.getId(), materialIds);
            model.addAttribute("favoriteMap", favoriteMap);
            model.addAttribute("loginUserId", loginUser.getId());
        }
        
        return "material/list";
    }
    
    private String getSortField(String sort) {
        return switch (sort) {
            case "name" -> "title";
            case "type" -> "type";
            case "newest" -> "id";
            default -> "id";
        };
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

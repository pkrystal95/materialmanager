package com.example.materialmanager.controller;

import com.example.materialmanager.common.Constants;
import com.example.materialmanager.common.SecurityUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /**
     * 자료 목록 조회
     */
    @GetMapping
    public String list(@RequestParam(required = false) Long lectureId,
                      @RequestParam(required = false) String title,
                      @RequestParam(required = false) String type,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "12") int size,
                      @RequestParam(defaultValue = "id") String sort,
                      @RequestParam(defaultValue = "desc") String direction,
                      HttpSession session, Model model) {
        
        if (!SecurityUtils.isLoggedIn(session)) {
            return Constants.REDIRECT_LOGIN;
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
        
        // 즐겨찾기 상태 조회
        User loginUser = SecurityUtils.getCurrentUser(session);
        if (loginUser != null) {
            List<Long> materialIds = materialPage.getContent().stream()
                .map(Material::getId)
                .toList();
            Map<Long, Boolean> favoriteMap = favoriteService.getFavoriteStatusMap(loginUser.getId(), materialIds);
            model.addAttribute("favoriteMap", favoriteMap);
            model.addAttribute("loginUserId", loginUser.getId());
        }
        
        return Constants.VIEW_MATERIALS_LIST;
    }
    
    private String getSortField(String sort) {
        return switch (sort) {
            case "name" -> "title";
            case "type" -> "type";
            case "newest" -> "id";
            default -> "id";
        };
    }

    /**
     * 자료 등록 폼
     */
    @GetMapping("/form")
    public String form(HttpSession session, Model model) {
        if (!SecurityUtils.isTeacherOrAdmin(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        model.addAttribute("material", new Material());
        model.addAttribute("lectures", lectureService.findAll());
        model.addAttribute("types", MaterialType.values());
        return Constants.VIEW_MATERIALS_FORM;
    }

    /**
     * 자료 등록 처리
     */
    @PostMapping("/form")
    public String submit(@Valid @ModelAttribute Material material,
                         @RequestParam(required = false) Long lectureId,
                         BindingResult bindingResult,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        
        if (!SecurityUtils.isTeacherOrAdmin(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        model.addAttribute("lectures", lectureService.findAll());
        model.addAttribute("types", MaterialType.values());
        
        if (bindingResult.hasErrors()) {
            return Constants.VIEW_MATERIALS_FORM;
        }
        
        // 강의 유효성 검증 (선택사항)
        if (lectureId != null) {
            lectureService.findById(lectureId)
                    .ifPresentOrElse(
                            lecture -> material.setLecture(lecture),
                            () -> bindingResult.rejectValue("lecture", "invalid", Constants.ERROR_LECTURE_NOT_FOUND)
                    );
        } else {
            // 강의가 선택되지 않은 경우 null로 설정
            material.setLecture(null);
        }
        
        if (bindingResult.hasErrors()) {
            return Constants.VIEW_MATERIALS_FORM;
        }
        
        try {
            materialService.save(material);
            redirectAttributes.addFlashAttribute("successMessage", "자료가 성공적으로 등록되었습니다.");
            
            // 강의가 선택된 경우 해당 강의로 필터링, 그렇지 않으면 전체 목록
            if (material.getLecture() != null) {
                return Constants.REDIRECT_MATERIALS + "?lectureId=" + material.getLecture().getId();
            } else {
                return Constants.REDIRECT_MATERIALS;
            }
        } catch (Exception e) {
            bindingResult.reject("global", "자료 등록 중 오류가 발생했습니다.");
            return Constants.VIEW_MATERIALS_FORM;
        }
    }

    /**
     * 자료 삭제
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!SecurityUtils.isTeacherOrAdmin(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        try {
            materialService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "자료가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "자료 삭제 중 오류가 발생했습니다.");
        }
        
        return Constants.REDIRECT_MATERIALS;
    }
    
}

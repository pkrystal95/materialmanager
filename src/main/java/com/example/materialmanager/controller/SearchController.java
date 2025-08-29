package com.example.materialmanager.controller;

import com.example.materialmanager.common.Constants;
import com.example.materialmanager.common.SecurityUtils;
import com.example.materialmanager.service.SearchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * 통합 검색
     */
    @GetMapping("/search")
    public String globalSearch(@RequestParam("q") String query, HttpSession session, Model model) {
        if (!SecurityUtils.isLoggedIn(session)) {
            return Constants.REDIRECT_LOGIN;
        }
        
        if (query == null || query.trim().isEmpty()) {
            return Constants.REDIRECT_HOME;
        }
        
        // 검색 수행
        SearchService.SearchResult searchResult = searchService.performGlobalSearch(query);
        
        // 모델에 검색 결과 추가
        model.addAttribute("searchResult", searchResult);
        model.addAttribute("query", searchResult.getQuery());
        model.addAttribute("materials", searchResult.getMaterials());
        model.addAttribute("lectures", searchResult.getLectures());
        model.addAttribute("totalResults", searchResult.getTotalResults());
        model.addAttribute("hasResults", searchResult.hasResults());
        model.addAttribute("materialCount", searchResult.getMaterialCount());
        model.addAttribute("lectureCount", searchResult.getLectureCount());
        
        return Constants.VIEW_SEARCH_RESULTS;
    }
}
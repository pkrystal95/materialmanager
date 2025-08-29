package com.example.materialmanager.service;

import com.example.materialmanager.common.ValidationUtils;
import com.example.materialmanager.domain.Lecture;
import com.example.materialmanager.domain.Material;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class SearchService {
    
    private final MaterialService materialService;
    private final LectureService lectureService;
    
    public SearchService(MaterialService materialService, LectureService lectureService) {
        this.materialService = materialService;
        this.lectureService = lectureService;
    }
    
    /**
     * 통합 검색 결과를 반환합니다.
     */
    public SearchResult performGlobalSearch(String query) {
        if (!ValidationUtils.isValidSearchQuery(query)) {
            return new SearchResult(query, List.of(), List.of());
        }
        
        String trimmedQuery = query.trim();
        
        // 자료 검색
        List<Material> materials = materialService.searchMaterials(trimmedQuery, null, null);
        
        // 강의 검색 (제목과 내용에서만 검색, 날짜는 null)
        List<Lecture> lectures = lectureService.searchLectures(trimmedQuery, trimmedQuery, null);
        
        return new SearchResult(trimmedQuery, materials, lectures);
    }
    
    /**
     * 자료만 검색합니다.
     */
    public List<Material> searchMaterialsOnly(String query, String type, Long lectureId) {
        if (!ValidationUtils.isValidSearchQuery(query)) {
            return List.of();
        }
        
        return materialService.searchMaterials(query.trim(), type, lectureId);
    }
    
    /**
     * 강의만 검색합니다.
     */
    public List<Lecture> searchLecturesOnly(String title, String content, String date) {
        if (!ValidationUtils.isValidSearchQuery(title) && 
            !ValidationUtils.isValidSearchQuery(content) && 
            ValidationUtils.isBlank(date)) {
            return List.of();
        }
        
        LocalDate parsedDate = parseDate(date);
        
        return lectureService.searchLectures(
            ValidationUtils.isNotBlank(title) ? title.trim() : null,
            ValidationUtils.isNotBlank(content) ? content.trim() : null, 
            parsedDate
        );
    }
    
    /**
     * 문자열 날짜를 LocalDate로 변환합니다.
     */
    private LocalDate parseDate(String dateString) {
        if (ValidationUtils.isBlank(dateString)) {
            return null;
        }
        
        try {
            return LocalDate.parse(dateString.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            // 날짜 파싱 실패시 null 반환
            return null;
        }
    }
    
    /**
     * 검색 결과를 담는 DTO 클래스
     */
    public static class SearchResult {
        private final String query;
        private final List<Material> materials;
        private final List<Lecture> lectures;
        private final int totalResults;
        
        public SearchResult(String query, List<Material> materials, List<Lecture> lectures) {
            this.query = query;
            this.materials = materials != null ? materials : List.of();
            this.lectures = lectures != null ? lectures : List.of();
            this.totalResults = this.materials.size() + this.lectures.size();
        }
        
        public String getQuery() { return query; }
        public List<Material> getMaterials() { return materials; }
        public List<Lecture> getLectures() { return lectures; }
        public int getTotalResults() { return totalResults; }
        public boolean hasResults() { return totalResults > 0; }
        public int getMaterialCount() { return materials.size(); }
        public int getLectureCount() { return lectures.size(); }
    }
}
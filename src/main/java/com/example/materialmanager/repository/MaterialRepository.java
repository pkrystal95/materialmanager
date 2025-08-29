package com.example.materialmanager.repository;

import com.example.materialmanager.domain.Material;
import com.example.materialmanager.domain.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByLectureId(Long lectureId);
    
    // 제목으로 검색 (대소문자 구분 없음)
    List<Material> findByTitleContainingIgnoreCase(String title);
    
    // 타입으로 검색
    List<Material> findByType(MaterialType type);
    
    // 복합 검색 - 제목, 타입, 강의로 검색
    @Query("SELECT m FROM Material m WHERE " +
           "(:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:type IS NULL OR m.type = :type) AND " +
           "(:lectureId IS NULL OR m.lecture.id = :lectureId)")
    List<Material> findMaterialsBySearch(@Param("title") String title, 
                                       @Param("type") MaterialType type, 
                                       @Param("lectureId") Long lectureId);
}

package com.example.materialmanager.repository;

import com.example.materialmanager.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByLectureDate(LocalDate lectureDate);
    
    // 제목으로 검색 (대소문자 구분 없음)
    List<Lecture> findByTitleContainingIgnoreCase(String title);
    
    // 내용으로 검색 (대소문자 구분 없음)
    List<Lecture> findByContentContainingIgnoreCase(String content);
    
    // 복합 검색 - 제목, 내용, 날짜로 검색
    @Query("SELECT l FROM Lecture l WHERE " +
           "(:title IS NULL OR LOWER(l.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:content IS NULL OR LOWER(l.content) LIKE LOWER(CONCAT('%', :content, '%'))) AND " +
           "(:date IS NULL OR l.lectureDate = :date)")
    List<Lecture> findLecturesBySearch(@Param("title") String title, 
                                     @Param("content") String content, 
                                     @Param("date") LocalDate date);
}

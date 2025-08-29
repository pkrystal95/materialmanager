package com.example.materialmanager.repository;

import com.example.materialmanager.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    // 강의별 자료 조회
    List<Material> findByLectureId(Long lectureId);
}

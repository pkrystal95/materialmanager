package com.example.materialmanager.repository;

import com.example.materialmanager.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByLectureIdOrderByUploadDateDesc(Long lectureId);
}

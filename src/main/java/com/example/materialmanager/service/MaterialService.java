package com.example.materialmanager.service;

import com.example.materialmanager.domain.Material;
import com.example.materialmanager.repository.MaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    // 모든 자료 조회
    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    // 강의별 자료 조회
    public List<Material> findByLectureId(Long lectureId) {
        return materialRepository.findByLectureId(lectureId);
    }

    // 자료 저장/등록
    public Material save(Material material) {
        return materialRepository.save(material);
    }

    // 자료 삭제
    public void delete(Long id) {
        materialRepository.deleteById(id);
    }
}

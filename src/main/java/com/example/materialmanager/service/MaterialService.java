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

    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    public List<Material> findByLectureId(Long lectureId) {
        return materialRepository.findByLectureId(lectureId);
    }

    public void save(Material material) {
        materialRepository.save(material);
    }

    public void delete(Long id) {
        materialRepository.deleteById(id);
    }
}

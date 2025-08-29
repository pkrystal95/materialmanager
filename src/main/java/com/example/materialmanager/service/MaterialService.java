package com.example.materialmanager.service;

import com.example.materialmanager.domain.Material;
import com.example.materialmanager.domain.MaterialType;
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
    
    public List<Material> searchMaterials(String title, String type, Long lectureId) {
        MaterialType materialType = null;
        if (type != null && !type.isEmpty()) {
            try {
                materialType = MaterialType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid type, ignore
            }
        }
        
        // Handle empty strings as null for the search
        String searchTitle = (title != null && title.trim().isEmpty()) ? null : title;
        
        return materialRepository.findMaterialsBySearch(searchTitle, materialType, lectureId);
    }
}

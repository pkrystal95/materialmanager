package com.example.materialmanager.service;

import com.example.materialmanager.domain.Favorite;
import com.example.materialmanager.domain.Material;
import com.example.materialmanager.domain.User;
import com.example.materialmanager.repository.FavoriteRepository;
import com.example.materialmanager.repository.MaterialRepository;
import com.example.materialmanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final MaterialRepository materialRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, 
                          UserRepository userRepository, 
                          MaterialRepository materialRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.materialRepository = materialRepository;
    }

    public List<Favorite> findByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }
    
    public void toggleFavorite(Long userId, Long materialId) {
        if (favoriteRepository.existsByUserIdAndMaterialId(userId, materialId)) {
            favoriteRepository.deleteByUserIdAndMaterialId(userId, materialId);
        } else {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));
            
            Favorite favorite = new Favorite(user, material);
            favoriteRepository.save(favorite);
        }
    }
    
    public boolean isFavorite(Long userId, Long materialId) {
        return favoriteRepository.existsByUserIdAndMaterialId(userId, materialId);
    }
    
    public Map<Long, Boolean> getFavoriteStatusMap(Long userId, List<Long> materialIds) {
        if (materialIds.isEmpty()) {
            return Map.of();
        }
        
        // 최적화: 필요한 materialId만 조회
        Set<Long> favoriteMaterialIds = Set.copyOf(
            favoriteRepository.findFavoriteMaterialIdsByUserIdAndMaterialIds(userId, materialIds)
        );
            
        return materialIds.stream()
            .collect(Collectors.toMap(
                id -> id,
                favoriteMaterialIds::contains
            ));
    }
}

package com.example.materialmanager.repository;

import com.example.materialmanager.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserIdAndMaterialId(Long userId, Long materialId);
    List<Favorite> findByUserId(Long userId);
}

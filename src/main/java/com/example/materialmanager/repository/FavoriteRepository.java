package com.example.materialmanager.repository;

import com.example.materialmanager.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserIdAndMaterialId(Long userId, Long materialId);
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndMaterialId(Long userId, Long materialId);
    void deleteByUserIdAndMaterialId(Long userId, Long materialId);
    
    @Query("SELECT f.material.id FROM Favorite f WHERE f.user.id = :userId AND f.material.id IN :materialIds")
    List<Long> findFavoriteMaterialIdsByUserIdAndMaterialIds(@Param("userId") Long userId, @Param("materialIds") List<Long> materialIds);
}

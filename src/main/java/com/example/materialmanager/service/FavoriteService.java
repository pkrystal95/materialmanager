package com.example.materialmanager.service;

import com.example.materialmanager.domain.Favorite;
import com.example.materialmanager.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public List<Favorite> findByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }
}

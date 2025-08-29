package com.example.materialmanager.dto;

import java.time.LocalDate;

public record MaterialDto(Long id, String title, String description, String link, String filePath, LocalDate uploadDate) {}

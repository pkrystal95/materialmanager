package com.example.materialmanager.dto;

import com.example.materialmanager.domain.Role;
import com.example.materialmanager.domain.User;

import java.time.format.DateTimeFormatter;

public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String createdAtFormatted;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAtFormatted = user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // getter
}

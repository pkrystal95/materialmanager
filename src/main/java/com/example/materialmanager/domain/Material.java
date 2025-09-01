package com.example.materialmanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "URL을 입력해주세요.")
    @Column(length = 2048)
    private String url;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "자료 타입을 선택해주세요.")
    private MaterialType type;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;
}

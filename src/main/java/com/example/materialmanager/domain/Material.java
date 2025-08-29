package com.example.materialmanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "URL을 입력해주세요.")
    private String url;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "자료 타입을 선택해주세요.")
    private MaterialType type;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    @NotNull(message = "강의를 선택해주세요.")
    private Lecture lecture;

    // 기본 생성자
    public Material() {}

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public MaterialType getType() { return type; }
    public void setType(MaterialType type) { this.type = type; }

    public Lecture getLecture() { return lecture; }
    public void setLecture(Lecture lecture) { this.lecture = lecture; }
}

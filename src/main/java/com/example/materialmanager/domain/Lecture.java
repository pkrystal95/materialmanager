package com.example.materialmanager.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;             // 강의 제목
    private LocalDate lectureDate;    // 수업 날짜
    private String content;           // 수업 내용

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL)
    private List<Material> materials = new ArrayList<>();

    public Lecture() {}

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getLectureDate() { return lectureDate; }
    public void setLectureDate(LocalDate lectureDate) { this.lectureDate = lectureDate; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<Material> getMaterials() { return materials; }
    public void setMaterials(List<Material> materials) { this.materials = materials; }
}

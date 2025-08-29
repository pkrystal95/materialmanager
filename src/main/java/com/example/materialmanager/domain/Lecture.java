package com.example.materialmanager.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate lectureDate;
    private String content;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Material> materials = new ArrayList<>();
}

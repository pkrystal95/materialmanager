package com.example.materialmanager.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","lecture_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private LocalDateTime enrolledAt;

    public Enrollment(User student, Lecture lecture) {
        this.student = student;
        this.lecture = lecture;
        this.enrolledAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (enrolledAt == null) enrolledAt = LocalDateTime.now();
    }
}

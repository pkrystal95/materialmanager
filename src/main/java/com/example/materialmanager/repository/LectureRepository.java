package com.example.materialmanager.repository;

import com.example.materialmanager.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByTeacherId(Long teacherId);
}

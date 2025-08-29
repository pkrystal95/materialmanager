package com.example.materialmanager.repository;

import com.example.materialmanager.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByLectureDate(LocalDate lectureDate);
}

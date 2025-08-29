package com.example.materialmanager.service;

import com.example.materialmanager.domain.Lecture;
import com.example.materialmanager.repository.LectureRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    public Optional<Lecture> findById(Long id) {
        return lectureRepository.findById(id);
    }

    public Lecture save(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    public void delete(Long id) {
        lectureRepository.deleteById(id);
    }

    public List<Lecture> findByDate(LocalDate date) {
        return lectureRepository.findByLectureDate(date);
    }
    
    public List<Lecture> searchLectures(String title, String content, LocalDate date) {
        // Handle empty strings as null for the search
        String searchTitle = (title != null && title.trim().isEmpty()) ? null : title;
        String searchContent = (content != null && content.trim().isEmpty()) ? null : content;
        
        return lectureRepository.findLecturesBySearch(searchTitle, searchContent, date);
    }
}

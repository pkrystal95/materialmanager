package com.example.materialmanager.service;

import com.example.materialmanager.domain.Lecture;
import com.example.materialmanager.repository.LectureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public List<Lecture> findAllLectures() {
        return lectureRepository.findAll();
    }

    public Lecture save(Lecture lecture) {
        return lectureRepository.save(lecture);
    }
}

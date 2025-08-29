package com.example.materialmanager.service;

import com.example.materialmanager.domain.Lecture;
import com.example.materialmanager.repository.LectureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    // 모든 강의 조회
    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    // ID로 강의 조회
    public Optional<Lecture> findById(Long id) {
        return lectureRepository.findById(id);
    }

    // 강의 저장/등록
    public Lecture save(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    // 강의 삭제
    public void delete(Long id) {
        lectureRepository.deleteById(id);
    }
}

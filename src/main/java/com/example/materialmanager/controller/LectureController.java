package com.example.materialmanager.controller;

import com.example.materialmanager.domain.Lecture;
import com.example.materialmanager.service.LectureService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String date, HttpSession session, Model model) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/auth/login";
        }

        List<Lecture> lectures;
        if (date != null && !date.isEmpty()) {
            lectures = lectureService.findByDate(java.time.LocalDate.parse(date));
        } else {
            lectures = lectureService.findAll();
        }
        model.addAttribute("lectures", lectures);
        return "lecture/list";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("lecture", new Lecture());
        return "lecture/form";
    }

    @PostMapping("/form")
    public String submit(@ModelAttribute Lecture lecture) {
        lectureService.save(lecture);
        return "redirect:/lectures";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Lecture lecture = lectureService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found"));
        model.addAttribute("lecture", lecture);
        return "lecture/form";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Long id, @ModelAttribute Lecture updatedLecture) {
        Lecture lecture = lectureService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found"));
        lecture.setTitle(updatedLecture.getTitle());
        lecture.setLectureDate(updatedLecture.getLectureDate());
        lecture.setContent(updatedLecture.getContent());
        lectureService.save(lecture);
        return "redirect:/lectures";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        lectureService.delete(id);
        return "redirect:/lectures";
    }
}

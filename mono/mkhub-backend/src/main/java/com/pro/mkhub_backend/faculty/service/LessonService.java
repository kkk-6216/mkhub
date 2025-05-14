package com.pro.mkhub_backend.faculty.service;


import com.pro.mkhub_backend.faculty.dto.lesson.*;
import com.pro.mkhub_backend.faculty.model.entity.Course;
import com.pro.mkhub_backend.faculty.model.entity.Lesson;
import com.pro.mkhub_backend.faculty.model.entity.LessonBlock;
import com.pro.mkhub_backend.security.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.pro.mkhub_backend.faculty.repository.LessonRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseService courseService;

    public List<Lesson> getLessons() {
        return lessonRepository.findAll();
    }

    public Lesson getLesson(Long id) {
        return lessonRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lesson with id " + id + " not found")
        );
    }

    public List<LessonInfoDto> getLessonsInfo() {
        return getLessons().stream()
                .map(LessonInfoDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Lesson createLesson(LessonCreationDto lessonCreationDto) {
        Course course = courseService.getCourse(lessonCreationDto.getCourseId());
        Lesson lesson = Lesson.builder()
                .title(lessonCreationDto.getName())
                .description(lessonCreationDto.getDescription())
                .course(course)
                .build();
        course.getLessonList().add(lesson);
        return lessonRepository.save(lesson);
    }

    @Transactional
    public Lesson updateLesson(Long id, LessonUpdateDto lessonUpdateDto) {
        Lesson lesson = getLesson(id);
        lesson.setTitle(lessonUpdateDto.getName());
        lesson.setDescription(lessonUpdateDto.getDescription());
        return lessonRepository.save(lesson);
    }

    @Transactional
    public void deleteLesson(Long id) {
        Lesson lesson = getLesson(id);
        lessonRepository.delete(lesson);
    }

}

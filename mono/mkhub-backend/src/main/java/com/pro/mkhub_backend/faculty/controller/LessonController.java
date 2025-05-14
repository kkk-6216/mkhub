package com.pro.mkhub_backend.faculty.controller;

import com.pro.mkhub_backend.faculty.dto.lesson.LessonCreationDto;
import com.pro.mkhub_backend.faculty.dto.lesson.LessonInfoDto;
import com.pro.mkhub_backend.faculty.dto.lesson.LessonUpdateDto;
import com.pro.mkhub_backend.faculty.model.entity.Lesson;
import com.pro.mkhub_backend.faculty.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    /**
     * Это для отображения списка уроков в странице модератора: Темы
     */
    @GetMapping
    public ResponseEntity<List<LessonInfoDto>> getLessons() {
        return ResponseEntity.ok(lessonService.getLessonsInfo());
    }

    /**
     * Это для создания урока в странице модератора: Темы
     */
    @PostMapping
    public ResponseEntity<LessonInfoDto> createLesson(@RequestBody LessonCreationDto lessonCreationDto) {
        Lesson lesson = lessonService.createLesson(lessonCreationDto);
        return ResponseEntity.ok(new LessonInfoDto(lesson));
    }

    /**
     * Это для создания урока в странице модератора: Темы
     */
    @PatchMapping("/{id}")
    public ResponseEntity<LessonInfoDto> updateLesson(@PathVariable Long id, @RequestBody LessonUpdateDto lessonUpdateDto) {
        Lesson lesson = lessonService.updateLesson(id, lessonUpdateDto);
        return ResponseEntity.ok(new LessonInfoDto(lesson));
    }

    /**
     * Это для удаления урока в странице модератора: Темы
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

}

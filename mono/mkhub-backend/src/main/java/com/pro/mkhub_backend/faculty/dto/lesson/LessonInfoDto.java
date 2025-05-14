package com.pro.mkhub_backend.faculty.dto.lesson;

import com.pro.mkhub_backend.faculty.model.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonInfoDto {

    private Long id;
    private String name;
    private String description;
    private CourseDto course;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    public static class CourseDto {
        private Long id;
        private String name;
    }

    public LessonInfoDto(Lesson lesson) {
        this.id = lesson.getId();
        this.name = lesson.getTitle();
        this.description = lesson.getDescription();
        this.createdAt = lesson.getCreatedAt();
        this.updatedAt = lesson.getUpdatedAt();

        this.course = CourseDto.builder()
                .id(lesson.getCourse().getId())
                .name(lesson.getCourse().getName())
                .build();
    }

}

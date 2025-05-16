package com.pro.mkhub_backend.faculty.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonCreationDto {

    private String name;
    private String description;
    private Long courseId;

}

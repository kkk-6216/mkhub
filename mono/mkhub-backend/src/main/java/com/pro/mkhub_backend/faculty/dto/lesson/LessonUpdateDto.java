package com.pro.mkhub_backend.faculty.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonUpdateDto {

    private Long id;
    private String name;
    private String description;

}

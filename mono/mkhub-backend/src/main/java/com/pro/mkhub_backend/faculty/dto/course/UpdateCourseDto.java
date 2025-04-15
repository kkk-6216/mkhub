package com.pro.mkhub_backend.faculty.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseDto {

    private Long id;
    private String name;
    private String description;

}

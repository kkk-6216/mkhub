package com.pro.mkhub_backend.faculty.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreationCourseDto {

    private String name;
    private String description;
    private Long departmentId;

}

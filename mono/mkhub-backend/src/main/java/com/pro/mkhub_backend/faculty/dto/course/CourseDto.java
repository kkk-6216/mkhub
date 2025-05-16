package com.pro.mkhub_backend.faculty.dto.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDto {

    private Long id;
    private String name;
    private String description;
    private DepartmentDto department;

    @Data
    @Builder
    public static class DepartmentDto {
        private Long id;
        private String name;
    }

}

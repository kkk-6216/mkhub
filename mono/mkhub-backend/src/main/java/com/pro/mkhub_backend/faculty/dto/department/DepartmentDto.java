package com.pro.mkhub_backend.faculty.dto.department;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentDto {

    private Long id;
    private String name;
    private String description;
    private FacultyDto faculty;

    @Data
    @Builder
    public static class FacultyDto {
        private Long id;
        private String name;
    }

}

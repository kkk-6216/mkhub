package com.pro.mkhub_backend.faculty.dto.faculty;

import lombok.Data;

import java.util.List;

@Data
public class FacultyDetailsDto {

    private Long id;
    private String name;
    private String description;
    private List<DepartmentDto> departments;

    @Data
    private static class DepartmentDto {
        private Long id;
        private String name;
    }

}

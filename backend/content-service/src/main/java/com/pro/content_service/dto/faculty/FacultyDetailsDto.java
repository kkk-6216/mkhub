package com.pro.content_service.dto.faculty;

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

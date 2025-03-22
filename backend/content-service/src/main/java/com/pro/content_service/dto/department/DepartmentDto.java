package com.pro.content_service.dto.department;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentDto {

    private Long id;
    private String name;
    private String description;
    private FacultyDto facultyDto;

    @Data
    @Builder
    public static class FacultyDto {
        private Long id;
        private String name;
    }

}

package com.pro.content_service.dto.faculty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacultyItemDto {
    private Long id;
    private String name;
}

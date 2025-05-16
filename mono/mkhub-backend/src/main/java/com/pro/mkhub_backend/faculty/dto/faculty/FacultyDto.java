package com.pro.mkhub_backend.faculty.dto.faculty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacultyDto {

    private Long id;
    private String name;
    private String description;

}

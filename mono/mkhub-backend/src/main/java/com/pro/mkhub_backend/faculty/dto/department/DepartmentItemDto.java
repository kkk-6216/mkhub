package com.pro.mkhub_backend.faculty.dto.department;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentItemDto {
    private Long id;
    private String name;
}

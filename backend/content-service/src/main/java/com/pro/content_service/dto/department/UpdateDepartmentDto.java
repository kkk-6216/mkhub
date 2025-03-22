package com.pro.content_service.dto.department;

import lombok.Data;

@Data
public class UpdateDepartmentDto {

    private Long id;
    private String name;
    private String description;
    private Long facultyId;

}

package com.pro.content_service.dto.department;

import lombok.Data;

@Data
public class CreationDepartmentDto {

    private String name;
    private String description;
    private Long facultyId;

}

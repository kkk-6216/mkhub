package com.pro.mkhub_backend.faculty.dto.department;

import lombok.Data;

@Data
public class CreationDepartmentDto {

    private String name;
    private String description;
    private Long facultyId;

}

package com.pro.content_service.mapper;

import com.pro.content_service.dto.department.DepartmentDto;
import com.pro.content_service.model.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentDto departmentToDepartmentDto(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .faculty(DepartmentDto.FacultyDto.builder()
                        .id(department.getFaculty().getId())
                        .name(department.getFaculty().getName())
                        .build())
                .build();
    }

}

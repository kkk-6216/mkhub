package com.pro.mkhub_backend.faculty.mapper;

import com.pro.mkhub_backend.faculty.dto.department.DepartmentDto;
import com.pro.mkhub_backend.faculty.dto.department.DepartmentItemDto;
import com.pro.mkhub_backend.faculty.model.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentDto departmentToDepartmentDto(Department department) {
        DepartmentDto.FacultyDto facultyDto = null;

        if (department.getFaculty() != null) {
            facultyDto = DepartmentDto.FacultyDto.builder()
                    .id(department.getFaculty().getId())
                    .name(department.getFaculty().getName())
                    .build();
        }

        return DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .faculty(facultyDto)
                .build();
    }

    public DepartmentItemDto departmentToDepartmentItemDto(Department department) {
        return DepartmentItemDto.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }


}

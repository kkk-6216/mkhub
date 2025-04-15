package com.pro.mkhub_backend.faculty.mapper;

import com.pro.mkhub_backend.faculty.dto.course.CourseDto;
import com.pro.mkhub_backend.faculty.dto.department.DepartmentDto;
import com.pro.mkhub_backend.faculty.model.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDto toCourseDto(Course course) {
        CourseDto.DepartmentDto department = null;

        if (course.getDepartment() != null) {
            department = CourseDto.DepartmentDto.builder()
                    .id(course.getDepartment().getId())
                    .name(course.getDepartment().getName())
                    .build();
        }

        return CourseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .department(department)
                .build();
    }

}

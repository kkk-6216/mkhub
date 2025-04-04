package com.pro.content_service.mapper;

import com.pro.content_service.dto.faculty.FacultyDto;
import com.pro.content_service.dto.faculty.FacultyItemDto;
import com.pro.content_service.model.entity.Faculty;
import org.springframework.stereotype.Component;

@Component
public class FacultyMapper {

    public FacultyDto facultyToFacultyDto(Faculty faculty) {
        return FacultyDto.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .description(faculty.getDescription())
                .build();
    }

    public FacultyItemDto facultyToFacultyItemDto(Faculty faculty) {
        return FacultyItemDto.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .build();
    }
}

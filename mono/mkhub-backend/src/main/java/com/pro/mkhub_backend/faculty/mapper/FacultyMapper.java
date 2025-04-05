package com.pro.mkhub_backend.faculty.mapper;

import com.pro.mkhub_backend.faculty.dto.faculty.FacultyDto;
import com.pro.mkhub_backend.faculty.dto.faculty.FacultyItemDto;
import com.pro.mkhub_backend.faculty.model.entity.Faculty;
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

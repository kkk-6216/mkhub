package com.pro.content_service.service;


import com.pro.content_service.dto.faculty.CreationFacultyDto;
import com.pro.content_service.dto.faculty.FacultyDto;
import com.pro.content_service.dto.faculty.UpdateFacultyDto;
import com.pro.content_service.exception.ResourceNotFoundException;
import com.pro.content_service.mapper.FacultyMapper;
import com.pro.content_service.model.entity.Faculty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.pro.content_service.repository.FacultyRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;

    @Transactional
    public FacultyDto createFaculty(CreationFacultyDto creationFacultyDto) {
        Faculty faculty = new Faculty();
        faculty.setName(creationFacultyDto.getName());
        faculty.setDescription(creationFacultyDto.getDescription());

        Faculty created = facultyRepository.save(faculty);

        return facultyMapper.facultyToFacultyDto(created);
    }

    public FacultyDto getFacultyById(Long id) {
        return facultyRepository.findById(id)
                .map(facultyMapper::facultyToFacultyDto)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id " + id));
    }

    @Transactional
    public FacultyDto updateFaculty(Long id, UpdateFacultyDto updateFacultyDto) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id " + id));
        faculty.setName(updateFacultyDto.getName());
        faculty.setDescription(updateFacultyDto.getDescription());
        Faculty updated = facultyRepository.save(faculty);

        return facultyMapper.facultyToFacultyDto(updated);
    }

    @Transactional
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public List<FacultyDto> getAllFaculties() {
        List<Faculty> facultyList = facultyRepository.findAll();
        return facultyList.stream()
                .map(facultyMapper::facultyToFacultyDto)
                .toList();
    }
}

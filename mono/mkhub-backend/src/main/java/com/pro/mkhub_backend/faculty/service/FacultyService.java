package com.pro.mkhub_backend.faculty.service;

import com.pro.mkhub_backend.faculty.dto.faculty.CreationFacultyDto;
import com.pro.mkhub_backend.faculty.dto.faculty.FacultyDto;
import com.pro.mkhub_backend.faculty.dto.faculty.FacultyItemDto;
import com.pro.mkhub_backend.faculty.dto.faculty.UpdateFacultyDto;
import com.pro.mkhub_backend.faculty.mapper.FacultyMapper;
import com.pro.mkhub_backend.faculty.model.entity.Faculty;
import com.pro.mkhub_backend.faculty.repository.FacultyRepository;
import com.pro.mkhub_backend.security.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        Faculty faculty = Faculty.builder()
                .name(creationFacultyDto.getName())
                .description(creationFacultyDto.getDescription())
                .build();

        Faculty created = facultyRepository.save(faculty);
        return facultyMapper.facultyToFacultyDto(created);
    }

    public FacultyDto getFacultyById(Long id) {
        Faculty faculty = findByIdOrThrow(id);
        return facultyMapper.facultyToFacultyDto(faculty);
    }

    @Transactional
    public FacultyDto updateFaculty(Long id, UpdateFacultyDto updateFacultyDto) {
        Faculty faculty = findByIdOrThrow(id);

        faculty.setName(updateFacultyDto.getName());
        faculty.setDescription(updateFacultyDto.getDescription());

        Faculty updated = facultyRepository.save(faculty);
        return facultyMapper.facultyToFacultyDto(updated);
    }

    @Transactional
    public void deleteFaculty(Long id) {
        Faculty faculty = findByIdOrThrow(id);
        facultyRepository.delete(faculty);
    }

    public List<FacultyDto> getAllFacultiesVerbose() {
        List<Faculty> faculties = facultyRepository.findAll();
        return faculties.stream()
                .map(facultyMapper::facultyToFacultyDto)
                .toList();
    }

    public List<FacultyItemDto> getAllFacultiesShort() {
        List<Faculty> faculties = facultyRepository.findAll();
        return faculties.stream()
                .map(facultyMapper::facultyToFacultyItemDto)
                .toList();
    }

    private Faculty findByIdOrThrow(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id " + id));
    }

}

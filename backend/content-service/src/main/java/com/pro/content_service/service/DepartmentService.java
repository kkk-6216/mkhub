package com.pro.content_service.service;


import com.pro.content_service.dto.department.CreationDepartmentDto;
import com.pro.content_service.dto.department.DepartmentDto;
import com.pro.content_service.dto.department.UpdateDepartmentDto;
import com.pro.content_service.exception.ResourceNotFoundException;
import com.pro.content_service.mapper.DepartmentMapper;
import com.pro.content_service.model.entity.Department;
import com.pro.content_service.model.entity.Faculty;
import com.pro.content_service.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.pro.content_service.repository.DepartmentRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public DepartmentDto createDepartment(CreationDepartmentDto departmentDto) {
        Faculty faculty = facultyRepository.findById(departmentDto.getFacultyId()).orElseThrow(
                () -> new ResourceNotFoundException("Faculty with id " + departmentDto.getFacultyId() + " not found")
        );
        Department department = Department.builder()
                .name(departmentDto.getName())
                .description(departmentDto.getDescription())
                .faculty(faculty)
                .build();
        faculty.getDepartments().add(department);

        Department savedDepartment = departmentRepository.save(department);

        return departmentMapper.departmentToDepartmentDto(savedDepartment);
    }

    public DepartmentDto getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::departmentToDepartmentDto)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id " + id + " not found"));
    }

    @Transactional
    public DepartmentDto updateDepartment(Long id, UpdateDepartmentDto departmentDto) {
        Department department = departmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Department with id " + id + " not found")
        );
        Faculty faculty = null;
        if(departmentDto.getFacultyId() != null && !departmentDto.getFacultyId().equals(department.getFaculty().getId())) {
            faculty = facultyRepository.findById(departmentDto.getFacultyId()).orElseThrow(
                    () -> new ResourceNotFoundException("Faculty with id " + departmentDto.getFacultyId() + " not found")
            );
        }

        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        if(faculty != null) {
            List<Department> departments = department.getFaculty().getDepartments();
            departments.remove(department);
            department.setFaculty(faculty);
            faculty.getDepartments().add(department);
        }

        departmentRepository.save(department);
        return departmentMapper.departmentToDepartmentDto(department);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id " + id + " not found!!!"));
        List<Department> departments = department.getFaculty().getDepartments();
        departments.remove(department);
        departmentRepository.deleteById(id);
    }

    public List<DepartmentDto> getDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::departmentToDepartmentDto)
                .toList();
    }

    public List<DepartmentDto> getDepartmentsByFacultyId(Long facultyId) {
        return departmentRepository.findByFacultyId(facultyId)
                .stream()
                .map(departmentMapper:: departmentToDepartmentDto)
                .toList();
    }
}

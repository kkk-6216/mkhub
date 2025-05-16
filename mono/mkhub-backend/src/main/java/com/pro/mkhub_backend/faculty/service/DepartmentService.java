package com.pro.mkhub_backend.faculty.service;

import com.pro.mkhub_backend.faculty.dto.department.CreationDepartmentDto;
import com.pro.mkhub_backend.faculty.dto.department.DepartmentDto;
import com.pro.mkhub_backend.faculty.dto.department.DepartmentItemDto;
import com.pro.mkhub_backend.faculty.dto.department.UpdateDepartmentDto;
import com.pro.mkhub_backend.faculty.mapper.DepartmentMapper;
import com.pro.mkhub_backend.faculty.model.entity.Department;
import com.pro.mkhub_backend.faculty.model.entity.Faculty;
import com.pro.mkhub_backend.faculty.repository.DepartmentRepository;
import com.pro.mkhub_backend.faculty.repository.FacultyRepository;
import com.pro.mkhub_backend.security.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public DepartmentDto createDepartment(CreationDepartmentDto departmentDto) {
        Faculty faculty = findFacultyById(departmentDto.getFacultyId());

        Department department = Department.builder()
                .name(departmentDto.getName())
                .description(departmentDto.getDescription())
                .build();

        faculty.addDepartment(department);

        return departmentMapper.departmentToDepartmentDto(departmentRepository.save(department));
    }

    public DepartmentDto getDepartmentById(Long id) {
        return departmentMapper.departmentToDepartmentDto(findDepartmentById(id));
    }

    @Transactional
    public DepartmentDto updateDepartment(Long id, UpdateDepartmentDto departmentDto) {
        Department department = findDepartmentById(id);
        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        // Переносим департамент, если facultyId изменился
        if (departmentDto.getFacultyId() != null &&
                !departmentDto.getFacultyId().equals(department.getFaculty().getId())) {

            Faculty newFaculty = findFacultyById(departmentDto.getFacultyId());
            Faculty oldFaculty = department.getFaculty();

            oldFaculty.removeDepartment(department);
            newFaculty.addDepartment(department);
        }

        return departmentMapper.departmentToDepartmentDto(departmentRepository.save(department));
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = findDepartmentById(id);
        Faculty faculty = department.getFaculty();

        faculty.removeDepartment(department);
        departmentRepository.delete(department);
    }

    public List<DepartmentDto> getDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::departmentToDepartmentDto)
                .toList();
    }

    public List<DepartmentDto> getDepartmentsByFacultyId(Long facultyId) {
        return departmentRepository.findByFacultyId(facultyId).stream()
                .map(departmentMapper::departmentToDepartmentDto)
                .toList();
    }

    private Department findDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
    }

    private Faculty findFacultyById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id " + id));
    }

    public List<DepartmentItemDto> getDepartmentsItem() {
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::departmentToDepartmentItemDto)
                .collect(Collectors.toList());
    }

}

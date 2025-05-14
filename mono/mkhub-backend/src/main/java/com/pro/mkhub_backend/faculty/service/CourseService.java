package com.pro.mkhub_backend.faculty.service;


import com.pro.mkhub_backend.faculty.dto.course.CourseDto;
import com.pro.mkhub_backend.faculty.dto.course.CreationCourseDto;
import com.pro.mkhub_backend.faculty.dto.course.UpdateCourseDto;
import com.pro.mkhub_backend.faculty.mapper.CourseMapper;
import com.pro.mkhub_backend.faculty.model.entity.Course;
import com.pro.mkhub_backend.faculty.model.entity.Department;
import com.pro.mkhub_backend.faculty.repository.DepartmentRepository;
import com.pro.mkhub_backend.security.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.pro.mkhub_backend.faculty.repository.CourseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseMapper courseMapper;

    public List<CourseDto> getCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toCourseDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getCoursesByDepartmentId(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId)
                .stream()
                .map(courseMapper::toCourseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseDto createCourse(CreationCourseDto courseDto) {
        Course course = new Course();
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());

        Department department = departmentRepository.findById(courseDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department with ID: %d not found".formatted(courseDto.getDepartmentId())));

        course.setDepartment(department);
        return courseMapper.toCourseDto(courseRepository.save(course));
    }

    @Transactional
    public CourseDto updateCourse(Long id, UpdateCourseDto courseDto) {
        Course course = getCourseById(id);
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        return courseMapper.toCourseDto(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourseById(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    private Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course with ID: %d not found".formatted(id))
        );
    }

    public Course getCourse(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));
    }
}

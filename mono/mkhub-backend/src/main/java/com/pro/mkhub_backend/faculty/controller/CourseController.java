package com.pro.mkhub_backend.faculty.controller;

import com.pro.mkhub_backend.faculty.dto.course.CourseDto;
import com.pro.mkhub_backend.faculty.dto.course.CreationCourseDto;
import com.pro.mkhub_backend.faculty.dto.course.UpdateCourseDto;
import com.pro.mkhub_backend.faculty.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses(@RequestParam(value = "departmentId", required = false) Long departmentId) {
        List<CourseDto> courses = (departmentId == null)
                ? courseService.getCourses()
                : courseService.getCoursesByDepartmentId(departmentId);

        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CreationCourseDto courseDto) {
        return ResponseEntity.ok(courseService.createCourse(courseDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @RequestBody UpdateCourseDto courseDto) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }

}

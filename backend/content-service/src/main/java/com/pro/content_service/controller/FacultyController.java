package com.pro.content_service.controller;

import com.pro.content_service.dto.faculty.CreationFacultyDto;
import com.pro.content_service.dto.faculty.FacultyDto;
import com.pro.content_service.dto.faculty.UpdateFacultyDto;
import com.pro.content_service.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculties")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    public ResponseEntity<FacultyDto> createFaculty(@RequestBody CreationFacultyDto creationFacultyDto) {
        FacultyDto createdFaculty = facultyService.createFaculty(creationFacultyDto);
        return ResponseEntity.ok(createdFaculty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyDto> getFacultyById(@PathVariable Long id) {
        FacultyDto faculty = facultyService.getFacultyById(id);
        return ResponseEntity.ok(faculty);
    }

    @GetMapping
    public ResponseEntity<List<?>> getAllFaculties(@RequestParam(value = "verbose", defaultValue = "true") boolean verbose) {
        List<?> facultyDtoList = facultyService.getAllFaculties(verbose);
        return ResponseEntity.ok(facultyDtoList);
    }


    @PutMapping("/{id}")
    public ResponseEntity<FacultyDto> updateFaculty(@PathVariable Long id, @RequestBody UpdateFacultyDto updateFacultyDto) {
        FacultyDto updatedFaculty = facultyService.updateFaculty(id, updateFacultyDto);
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}

package com.pro.mkhub_backend.faculty.controller;

import com.pro.mkhub_backend.faculty.dto.faculty.CreationFacultyDto;
import com.pro.mkhub_backend.faculty.dto.faculty.FacultyDto;
import com.pro.mkhub_backend.faculty.dto.faculty.FacultyItemDto;
import com.pro.mkhub_backend.faculty.dto.faculty.UpdateFacultyDto;
import com.pro.mkhub_backend.faculty.service.FacultyService;
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
    public ResponseEntity<?> getAllFaculties(@RequestParam(value = "verbose", defaultValue = "true") boolean verbose) {
        if (verbose) {
            List<FacultyDto> fullList = facultyService.getAllFacultiesVerbose();
            return ResponseEntity.ok(fullList);
        } else {
            List<FacultyItemDto> shortList = facultyService.getAllFacultiesShort();
            return ResponseEntity.ok(shortList);
        }
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

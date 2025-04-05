package com.pro.mkhub_backend.faculty.controller;

import com.pro.mkhub_backend.faculty.dto.department.CreationDepartmentDto;
import com.pro.mkhub_backend.faculty.dto.department.DepartmentDto;
import com.pro.mkhub_backend.faculty.dto.department.UpdateDepartmentDto;
import com.pro.mkhub_backend.faculty.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody CreationDepartmentDto departmentDto) {
        DepartmentDto savedDepartment = departmentService.createDepartment(departmentDto);
        return ResponseEntity.ok(savedDepartment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(
            @PathVariable Long id,
            @RequestBody UpdateDepartmentDto departmentDto) {

        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getDepartments(
            @RequestParam(value = "facultyId", required = false) Long facultyId) {

        List<DepartmentDto> departments = (facultyId == null)
                ? departmentService.getDepartments()
                : departmentService.getDepartmentsByFacultyId(facultyId);

        return ResponseEntity.ok(departments);
    }

}


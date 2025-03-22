package com.pro.content_service.controller;

import com.pro.content_service.dto.department.CreationDepartmentDto;
import com.pro.content_service.dto.department.DepartmentDto;
import com.pro.content_service.dto.department.UpdateDepartmentDto;
import com.pro.content_service.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
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
        DepartmentDto department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id,
                                                          @RequestBody UpdateDepartmentDto departmentDto) {
        DepartmentDto updatedDepartment = departmentService.updateDepartment(id, departmentDto);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getDepartments(@RequestParam(value = "facultyId", required = false) Long facultyId) {
        List<DepartmentDto> departments = null;

        if(facultyId == null) {
            departments = departmentService.getDepartments();
        } else {
            departments = departmentService.getDepartmentsByFacultyId(facultyId);
        }

        return ResponseEntity.ok(departments);
    }


}


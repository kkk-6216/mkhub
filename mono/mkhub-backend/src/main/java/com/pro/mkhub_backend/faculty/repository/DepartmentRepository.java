package com.pro.mkhub_backend.faculty.repository;

import com.pro.mkhub_backend.faculty.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByFacultyId(Long facultyId);

}
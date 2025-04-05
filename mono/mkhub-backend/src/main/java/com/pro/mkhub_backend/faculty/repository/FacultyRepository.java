package com.pro.mkhub_backend.faculty.repository;

import com.pro.mkhub_backend.faculty.model.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
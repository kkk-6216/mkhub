package com.pro.mkhub_backend.faculty.repository;

import com.pro.mkhub_backend.faculty.model.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT l FROM Lesson l JOIN FETCH l.blocks WHERE l.id = :id")
    Optional<Lesson> findByIdWithBlocks(@Param("id") Long id);

}
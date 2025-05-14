package com.pro.mkhub_backend.faculty.repository;

import com.pro.mkhub_backend.faculty.model.entity.LessonBlockDraft;
import com.pro.mkhub_backend.file_storage.model.enums.ModerationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LessonBlockDraftRepository extends JpaRepository<LessonBlockDraft, Long> {
    List<LessonBlockDraft> findAllByStatus(ModerationStatus moderationStatus);
}
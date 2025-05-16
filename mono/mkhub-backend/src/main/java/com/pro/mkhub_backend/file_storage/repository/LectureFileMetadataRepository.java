package com.pro.mkhub_backend.file_storage.repository;

import com.pro.mkhub_backend.file_storage.model.entity.LectureFileMetadata;
import com.pro.mkhub_backend.file_storage.model.enums.ModerationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureFileMetadataRepository extends JpaRepository<LectureFileMetadata, Long> {

    List<LectureFileMetadata> findByLectureId(Long lectureId);
    List<LectureFileMetadata> findByModerationStatus(ModerationStatus moderationStatus);

}

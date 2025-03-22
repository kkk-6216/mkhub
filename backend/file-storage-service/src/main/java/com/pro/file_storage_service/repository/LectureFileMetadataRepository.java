package com.pro.file_storage_service.repository;

import com.pro.file_storage_service.model.entity.LectureFileMetadata;
import com.pro.file_storage_service.model.enums.ModerationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureFileMetadataRepository extends JpaRepository<LectureFileMetadata, Long> {

    List<LectureFileMetadata> findByLectureId(Long lectureId);
    List<LectureFileMetadata> findByModerationStatus(ModerationStatus moderationStatus);

}

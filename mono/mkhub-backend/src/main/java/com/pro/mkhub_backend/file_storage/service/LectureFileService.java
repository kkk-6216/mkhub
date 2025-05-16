package com.pro.mkhub_backend.file_storage.service;

import com.pro.mkhub_backend.file_storage.model.entity.LectureFileMetadata;
import com.pro.mkhub_backend.file_storage.model.enums.FileCategory;
import com.pro.mkhub_backend.file_storage.model.enums.ModerationStatus;
import com.pro.mkhub_backend.file_storage.repository.LectureFileMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureFileService {

    private final LectureFileMetadataRepository lectureFileMetadataRepository;
    private final GoogleDriveService googleDriveService;

    @Transactional
    public LectureFileMetadata uploadLectureFile(Long lectureId, FileCategory category, MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл не должен быть пустым.");
        }

        String lectureFolderPath = "/lectures/" + lectureId;
        String categoryFolderPath = lectureFolderPath + "/" + category.getValue();

        googleDriveService.getOrCreateFolder(lectureFolderPath);
        String categoryFolderId = googleDriveService.getOrCreateFolder(categoryFolderPath);

        GoogleDriveService.FileUploadResult result = googleDriveService.uploadFile(file, categoryFolderId);

        LectureFileMetadata metadata = new LectureFileMetadata();
        metadata.setLectureId(lectureId);
        metadata.setCategory(category);
        metadata.setGoogleDriveFileId(result.getFileId());
        metadata.setFileName(file.getOriginalFilename());
        metadata.setStoredFileName(result.getStoredFileName());
        metadata.setMimeType(file.getContentType());
        metadata.setFileSize(file.getSize());
        metadata.setStoragePath(result.getStoragePath());
        metadata.setUploadDate(LocalDateTime.now());
        metadata.setLastModified(LocalDateTime.now());

        metadata.setModerationStatus(ModerationStatus.PENDING);
        metadata.setIsOriginal(false);

        log.info("Файл '{}' успешно загружен в лекцию {} (категория {})", file.getOriginalFilename(), lectureId, category);

        return lectureFileMetadataRepository.save(metadata);
    }

}

package com.pro.file_storage_service.service;

import com.pro.file_storage_service.model.entity.LectureFileMetadata;
import com.pro.file_storage_service.model.enums.FileCategory;
import com.pro.file_storage_service.model.enums.ModerationStatus;
import com.pro.file_storage_service.repository.LectureFileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LectureFileService {

    private final LectureFileMetadataRepository lectureFileMetadataRepository;
    private final GoogleDriveService googleDriveService;

    @Transactional
    public LectureFileMetadata uploadLectureFile(Long lectureId, FileCategory category, MultipartFile file) {

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

        return lectureFileMetadataRepository.save(metadata);
    }
}

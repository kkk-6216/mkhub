package com.pro.file_storage_service.service;

import com.pro.file_storage_service.model.entity.UserFileMetadata;
import com.pro.file_storage_service.repository.UserFileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFileService {

    private final UserFileMetadataRepository userFileMetadataRepository;
    private final GoogleDriveService googleDriveService;

    @Transactional
    public UserFileMetadata uploadUserAvatar(Long userId, MultipartFile file) {

        String userFolderPath = "/users/" + userId;
        String avatarFolderPath = userFolderPath + "/avatar";

        googleDriveService.getOrCreateFolder(userFolderPath);
        String avatarFolderId = googleDriveService.getOrCreateFolder(avatarFolderPath);

        GoogleDriveService.FileUploadResult result = googleDriveService.uploadFile(file, avatarFolderId);

        UserFileMetadata metadata = new UserFileMetadata();
        metadata.setUserId(userId);
        metadata.setGoogleDriveFileId(result.getFileId());
        metadata.setFileName(file.getOriginalFilename());
        metadata.setStoredFileName(result.getStoredFileName());
        metadata.setMimeType(file.getContentType());
        metadata.setFileSize(file.getSize());
        metadata.setStoragePath(result.getStoragePath());
        metadata.setUploadDate(LocalDateTime.now());
        metadata.setLastModified(LocalDateTime.now());

        return userFileMetadataRepository.save(metadata);
    }

    public UserFileMetadata getUserAvatar(Long userId) {
        return userFileMetadataRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Avatar not found for user id: " + userId));
    }

    @Transactional
    public UserFileMetadata updateUserAvatar(Long userId, MultipartFile file) {

        Optional<UserFileMetadata> existingAvatarOpt = userFileMetadataRepository.findByUserId(userId);
        existingAvatarOpt.ifPresent(existingAvatar -> {

            googleDriveService.deleteFile(existingAvatar.getGoogleDriveFileId());

            userFileMetadataRepository.delete(existingAvatar);
        });

        return uploadUserAvatar(userId, file);
    }

}

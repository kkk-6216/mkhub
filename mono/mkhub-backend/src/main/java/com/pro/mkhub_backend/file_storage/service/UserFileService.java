package com.pro.mkhub_backend.file_storage.service;

import com.pro.mkhub_backend.file_storage.model.entity.UserFileMetadata;
import com.pro.mkhub_backend.file_storage.repository.UserFileMetadataRepository;
import com.pro.mkhub_backend.user.model.entity.UserInfo;
import com.pro.mkhub_backend.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFileService {

    private final UserFileMetadataRepository userFileMetadataRepository;
    private final GoogleDriveService googleDriveService;
    private final UserInfoRepository userInfoRepository;

    @Transactional
    public UserFileMetadata uploadUserAvatar(Long userId, MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл не должен быть пустым.");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Разрешены только изображения (image/*).");
        }

        UserInfo userInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID %d not found".formatted(userId)));


        UserFileMetadata oldAvatar = userInfo.getUserFileMetadata();
        if (oldAvatar != null) {
            googleDriveService.deleteFile(oldAvatar.getGoogleDriveFileId());
            userFileMetadataRepository.delete(oldAvatar);
        }

        String userFolderPath = "/users/" + userId;
        String avatarFolderPath = userFolderPath + "/avatar";

        String userFolderPathCreated = googleDriveService.getOrCreateFolder(userFolderPath);
        String avatarFolderId = googleDriveService.getOrCreateFolder(avatarFolderPath);

        GoogleDriveService.FileUploadResult result = googleDriveService.uploadFile(file, avatarFolderId);

        UserFileMetadata metadata = new UserFileMetadata();
        metadata.setUserFolderPath(userFolderPathCreated);
        metadata.setGoogleDriveFileId(result.getFileId());
        metadata.setFileName(file.getOriginalFilename());
        metadata.setStoredFileName(result.getStoredFileName());
        metadata.setMimeType(file.getContentType());
        metadata.setFileSize(file.getSize());
        metadata.setStoragePath(result.getStoragePath());
        metadata.setUploadDate(LocalDateTime.now());
        metadata.setLastModified(LocalDateTime.now());

        metadata.setUserInfo(userInfo);
        userInfo.setUserFileMetadata(metadata);

        userFileMetadataRepository.save(metadata);

        log.info("Пользователь {} загрузил новый аватар: {}", userId, metadata.getStoredFileName());

        return metadata;
    }


    public UserFileMetadata getUserAvatar(Long userId) {
        return userFileMetadataRepository.findByUserInfoId(userId)
                .orElseThrow(() -> new RuntimeException("Аватар не найден для пользователя с ID: " + userId));
    }

    @Transactional
    public UserFileMetadata updateUserAvatar(Long userId, MultipartFile file) {
        return uploadUserAvatar(userId, file);
    }

}

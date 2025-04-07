package com.pro.mkhub_backend.file_storage.listener;

import com.pro.mkhub_backend.file_storage.model.entity.UserFileMetadata;
import com.pro.mkhub_backend.file_storage.service.GoogleDriveService;
import jakarta.persistence.PreRemove;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFileMetadataListener {

    private final GoogleDriveService googleDriveService;

    @PreRemove
    public void preRemove(UserFileMetadata metadata) {
        googleDriveService.deleteFile(metadata.getGoogleDriveFileId());
        googleDriveService.deleteFile(metadata.getUserFolderPath());
    }

}


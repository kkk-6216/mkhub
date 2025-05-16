package com.pro.mkhub_backend.file_storage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ContentDraftService {

    private final GoogleDriveService googleDriveService;

    public String uploadImageForLesson(Long lessonId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        String baseName = "image";

        if (originalFilename != null && originalFilename.contains(".")) {
            int dotIndex = originalFilename.lastIndexOf(".");
            baseName = originalFilename.substring(0, dotIndex);
            extension = originalFilename.substring(dotIndex);
        } else if (originalFilename != null) {
            baseName = originalFilename;
        }

        String uniqueFilename = baseName + "_" + UUID.randomUUID() + extension;

        String imageFolderPath = "/lesson_content/" + lessonId + "/images";
        String imageFolderId = googleDriveService.getOrCreateFolder(imageFolderPath);

        GoogleDriveService.FileUploadResult result = googleDriveService.uploadFile(file, imageFolderId, uniqueFilename);

        return result.getFileId();
    }


    public String uploadFileForLesson(Long lessonId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        String baseName = "file";

        if (originalFilename != null && originalFilename.contains(".")) {
            int dotIndex = originalFilename.lastIndexOf(".");
            baseName = originalFilename.substring(0, dotIndex);
            extension = originalFilename.substring(dotIndex);
        } else if (originalFilename != null) {
            baseName = originalFilename;
        }

        String uniqueFilename = baseName + "_" + UUID.randomUUID() + extension;

        String fileFolderPath = "/lesson_content/" + lessonId + "/files";
        String fileFolderId = googleDriveService.getOrCreateFolder(fileFolderPath);

        GoogleDriveService.FileUploadResult result = googleDriveService.uploadFile(file, fileFolderId, uniqueFilename);

        return result.getFileId();
    }
}

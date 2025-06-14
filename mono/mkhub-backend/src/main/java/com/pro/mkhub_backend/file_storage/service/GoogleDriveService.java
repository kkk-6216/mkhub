package com.pro.mkhub_backend.file_storage.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveService {

    private final Drive googleDrive;

    @Value("${google.drive.root-folder-id}")
    private String rootFolderId;

    public String getOrCreateFolder(String folderPath) {

        String parentId = rootFolderId;
        String[] folders = folderPath.split("/");
        for (String folderName : folders) {
            if (folderName.isEmpty()) {
                continue;
            }

            String folderId = findFolderIdByName(folderName, parentId);
            if (folderId == null) {
                folderId = createFolder(folderName, parentId);
            }
            parentId = folderId;
        }
        return parentId;
    }

    public FileUploadResult uploadFile(MultipartFile file, String parentFolderId) {
        try (InputStream inputStream = file.getInputStream()) {

            File fileMetadata = new File();
            fileMetadata.setName(file.getOriginalFilename());
            return getFileUploadResult(file, parentFolderId, inputStream, fileMetadata);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла в Google Drive", e);
        }
    }

    private FileUploadResult getFileUploadResult(MultipartFile file, String parentFolderId, InputStream inputStream, File fileMetadata) throws IOException {
        fileMetadata.setParents(Collections.singletonList(parentFolderId));

        InputStreamContent mediaContent = new InputStreamContent(file.getContentType(), inputStream);

        File uploadedFile = googleDrive.files().create(fileMetadata, mediaContent)
                .setFields("id, name")
                .execute();

        String storagePath = parentFolderId + "/" + uploadedFile.getName();

        FileUploadResult result = new FileUploadResult();
        result.setFileId(uploadedFile.getId());
        result.setStoredFileName(uploadedFile.getName());
        result.setStoragePath(storagePath);

        return result;
    }

    public FileUploadResult uploadFile(MultipartFile file, String parentFolderId, String desiredFilename) {
        try (InputStream inputStream = file.getInputStream()) {

            File fileMetadata = new File();
            fileMetadata.setName(desiredFilename);
            return getFileUploadResult(file, parentFolderId, inputStream, fileMetadata);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла в Google Drive", e);
        }
    }


    private String findFolderIdByName(String folderName, String parentId) {
        try {
            String query = "mimeType = 'application/vnd.google-apps.folder' and trashed = false " +
                    "and name = '" + folderName + "' and '" + parentId + "' in parents";
            FileList result = googleDrive.files().list()
                    .setQ(query)
                    .setFields("files(id, name)")
                    .execute();
            if (result.getFiles() != null && !result.getFiles().isEmpty()) {
                return result.getFiles().get(0).getId();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка поиска папки в Google Drive", e);
        }
        return null;
    }

    private String createFolder(String folderName, String parentId) {
        try {
            File fileMetadata = new File();
            fileMetadata.setName(folderName);
            fileMetadata.setMimeType("application/vnd.google-apps.folder");
            fileMetadata.setParents(Collections.singletonList(parentId));

            File folder = googleDrive.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            return folder.getId();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания папки в Google Drive", e);
        }
    }

    public void deleteFile(String fileId) {
        try {
            googleDrive.files().delete(fileId).execute();
        } catch (NullPointerException | GoogleJsonResponseException e) {
            log.warn(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка удаления файла из Google Drive. FileId: " + fileId, e);
        }
    }

    @Data
    public static class FileUploadResult {

        private String fileId;
        private String storedFileName;
        private String storagePath;

    }

}

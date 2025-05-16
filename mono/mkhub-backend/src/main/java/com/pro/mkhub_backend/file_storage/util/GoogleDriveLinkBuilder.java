package com.pro.mkhub_backend.file_storage.util;

import org.springframework.stereotype.Component;

@Component
public class GoogleDriveLinkBuilder {

    public String buildThumbnailUrl(String fileId) {
        return "https://drive.google.com/thumbnail?id=" + fileId;
    }

    public String buildDirectDownloadUrl(String fileId) {
        return "https://drive.google.com/uc?export=download&id=" + fileId;
    }

    public String buildPreviewUrl(String fileId) {
        return "https://drive.google.com/file/d/" + fileId + "/preview";
    }

    public String buildOpenUrl(String fileId) {
        return "https://drive.google.com/open?id=" + fileId;
    }
}

package com.pro.file_storage_service.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileCategory {

    IMAGES("images"),
    DOCS("docs");

    private final String value;

}

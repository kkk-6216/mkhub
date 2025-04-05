package com.pro.mkhub_backend.file_storage.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileCategory {

    IMAGES("images"),
    DOCS("docs");

    private final String value;

}

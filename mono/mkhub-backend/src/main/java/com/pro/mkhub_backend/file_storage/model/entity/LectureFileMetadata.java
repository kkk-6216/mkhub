package com.pro.mkhub_backend.file_storage.model.entity;

import com.pro.mkhub_backend.file_storage.model.enums.FileCategory;
import com.pro.mkhub_backend.file_storage.model.enums.ModerationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lecture_files")
public class LectureFileMetadata extends FileMetadata {

    @Column(nullable = false)
    private Long lectureId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModerationStatus moderationStatus;

    private LocalDateTime moderationDate;

    private Long moderatorId;

    private Boolean isOriginal;
}

package com.pro.mkhub_backend.faculty.controller;

import com.pro.mkhub_backend.faculty.dto.draft.LessonBlockDraftCreationDto;
import com.pro.mkhub_backend.faculty.dto.draft.LessonDraftCreationDto;
import com.pro.mkhub_backend.faculty.mapper.LessonDraftMapper;
import com.pro.mkhub_backend.faculty.service.LessonBlockDraftService;
import com.pro.mkhub_backend.file_storage.service.ContentDraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/lessons/{lessonId}")
@RequiredArgsConstructor
public class ContentBlockDraftController {

    private final LessonBlockDraftService lessonBlockDraftService;
    private final LessonDraftMapper lessonDraftMapper;

    @PostMapping("/offer")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_STUDENT')")
    public ResponseEntity<?> offer(@PathVariable("lessonId") Long lessonId,
                                   @RequestPart("userId") String authorId,
                                   @RequestPart("blocks") String blocksJson,
                                   @RequestPart MultipartFile[] files
                                   ) throws IOException {
        Map<String, MultipartFile> fileMap = new HashMap<>();
        if (files != null) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String key = file.getName();
                    fileMap.put(key, file);
                }
            }
        }
        LessonDraftCreationDto draft = lessonDraftMapper.mapFromRequest(blocksJson, lessonId, fileMap);
        lessonBlockDraftService.saveDraft(draft, Long.valueOf(authorId));

        return ResponseEntity.ok().build();
    }

}

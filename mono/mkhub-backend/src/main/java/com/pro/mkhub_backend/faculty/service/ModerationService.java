package com.pro.mkhub_backend.faculty.service;

import com.pro.mkhub_backend.faculty.repository.LessonBlockDraftRepository;
import com.pro.mkhub_backend.faculty.repository.LessonBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModerationService {

    private final LessonBlockDraftRepository lessonBlockDraftRepository;
    private final LessonBlockRepository lessonBlockRepository;

}

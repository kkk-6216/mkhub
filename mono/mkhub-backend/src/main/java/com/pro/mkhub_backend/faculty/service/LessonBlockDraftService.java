package com.pro.mkhub_backend.faculty.service;


import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.auth.service.UserService;
import com.pro.mkhub_backend.faculty.dto.draft.LessonBlockDraftCreationDto;
import com.pro.mkhub_backend.faculty.dto.draft.LessonDraftCreationDto;
import com.pro.mkhub_backend.faculty.model.entity.Lesson;
import com.pro.mkhub_backend.faculty.model.entity.LessonBlockDraft;
import com.pro.mkhub_backend.file_storage.model.enums.ModerationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.pro.mkhub_backend.faculty.repository.LessonBlockDraftRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonBlockDraftService {

    private final LessonBlockDraftRepository lessonBlockDraftRepository;
    private final LessonService lessonService;
    private final UserService userService;

    public void saveDraft(LessonDraftCreationDto draft, Long authorId) {
        List<LessonBlockDraftCreationDto> blockDtos = draft.getBlocks();

        Lesson lesson = lessonService.getLesson(draft.getId());
        User author = userService.findById(authorId);

        List<LessonBlockDraft> entities = new ArrayList<>();

        for (LessonBlockDraftCreationDto dto : blockDtos) {
            LessonBlockDraft entity = new LessonBlockDraft();

            entity.setLesson(lesson);
            entity.setType(dto.getType());
            entity.setData(dto.getData());
            entity.setOrderIndex(dto.getOrder());
            entity.setStatus(ModerationStatus.PENDING);

            entity.setCreatedBy(author);

            entities.add(entity);
        }

        lessonBlockDraftRepository.saveAll(entities);
    }

    public List<LessonBlockDraft> getAllByStatus(ModerationStatus status) {
        return lessonBlockDraftRepository.findAllByStatus(status);
    }

}

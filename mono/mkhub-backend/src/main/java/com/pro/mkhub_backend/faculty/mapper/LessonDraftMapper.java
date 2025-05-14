package com.pro.mkhub_backend.faculty.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.mkhub_backend.faculty.dto.draft.LessonBlockDraftCreationDto;
import com.pro.mkhub_backend.faculty.dto.draft.LessonDraftCreationDto;
import com.pro.mkhub_backend.faculty.model.enums.LessonBlockType;
import com.pro.mkhub_backend.file_storage.service.ContentDraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LessonDraftMapper {

    private final ObjectMapper objectMapper;
    private final ContentDraftService contentDraftService;

    public LessonDraftCreationDto mapFromRequest(
            String blocksJson,
            Long lessonId,
            Map<String, MultipartFile> files
    ) throws IOException {

        LessonDraftCreationDto request = new LessonDraftCreationDto();
        request.setId(lessonId);

        List<LessonBlockDraftCreationDto> blocks = objectMapper.readValue(
                blocksJson,
                new TypeReference<>() {}
        );

        for (LessonBlockDraftCreationDto block : blocks) {
            LessonBlockType type = block.getType();

            switch (type) {
                case IMAGE:
                case FILE:
                    MultipartFile file = files.get(block.getData());
                    String data = block.getData();

                    boolean isFilePresent = file != null && !file.isEmpty();
                    boolean isTextLinkPresent = data != null && !data.trim().isEmpty();

                    if (!isFilePresent && !isTextLinkPresent) {
                        throw new IllegalArgumentException("Файл или ссылка обязателен для блока типа " + type);
                    }
                    break;

                case TEXT:
                case LINK:
                case MATH:
                case CODE:
                    String text = block.getData();
                    if (text == null || text.trim().isEmpty()) {
                        throw new IllegalArgumentException("Текст обязателен для блока типа " + type);
                    }
                    System.out.println(text);
                    break;

                default:
                    throw new IllegalArgumentException("Неизвестный тип блока: " + type);
            }
        }


        for (LessonBlockDraftCreationDto block : blocks) {
            if (block.getType() == LessonBlockType.IMAGE) {
                MultipartFile file = files.get(block.getData());
                if (file != null) {
                    System.out.println(file.getOriginalFilename());
                    String uploadedUrl = contentDraftService.uploadImageForLesson(lessonId, file);
                    block.setData(uploadedUrl);
                }
            } else if (block.getType() == LessonBlockType.FILE) {
                MultipartFile file = files.get(block.getData());
                if (file != null) {
                    System.out.println(file.getOriginalFilename());
                    String uploadedUrl = contentDraftService.uploadFileForLesson(lessonId, file);
                    block.setData(uploadedUrl);
                }
            }
        }

        request.setBlocks(blocks);
        return request;
    }

}

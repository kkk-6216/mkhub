package com.pro.mkhub_backend.faculty.dto.draft;

import com.pro.mkhub_backend.faculty.model.enums.LessonBlockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonBlockDraftCreationDto {

    private LessonBlockType type;
    private Integer order;
    private String data;

}

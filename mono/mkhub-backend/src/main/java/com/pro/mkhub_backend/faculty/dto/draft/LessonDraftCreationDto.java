package com.pro.mkhub_backend.faculty.dto.draft;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDraftCreationDto {

    private Long id;
    private List<LessonBlockDraftCreationDto> blocks;

}

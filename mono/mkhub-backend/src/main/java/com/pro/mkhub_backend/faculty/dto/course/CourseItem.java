package com.pro.mkhub_backend.faculty.dto.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseItem {

    private Long id;
    private String name;

    public CourseItem(CourseDto courseDto) {
        this.id = courseDto.getId();
        this.name = courseDto.getName();
    }

}

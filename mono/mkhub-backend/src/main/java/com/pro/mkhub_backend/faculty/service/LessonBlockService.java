package com.pro.mkhub_backend.faculty.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.pro.mkhub_backend.faculty.repository.LessonBlockRepository;

@Service
@RequiredArgsConstructor
public class LessonBlockService {

    private final LessonBlockRepository lessonBlockRepository;

}

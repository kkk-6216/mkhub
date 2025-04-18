package com.pro.mkhub_backend.faculty.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.pro.mkhub_backend.faculty.repository.LessonRepository;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

}

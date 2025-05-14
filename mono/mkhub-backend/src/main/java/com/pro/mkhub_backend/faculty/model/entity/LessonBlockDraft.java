package com.pro.mkhub_backend.faculty.model.entity;

import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.faculty.model.enums.LessonBlockType;
import com.pro.mkhub_backend.file_storage.model.enums.ModerationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "lesson_block_draft")
public class LessonBlockDraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LessonBlockType type;

    @Lob
    private String data;

    @Enumerated(EnumType.STRING)
    private ModerationStatus status = ModerationStatus.PENDING;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User reviewedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime reviewedAt;

    private String rejectionReason;

    @CreationTimestamp
    private LocalDateTime createdAt;

}

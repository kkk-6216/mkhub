package com.pro.mkhub_backend.user.model.entity;

import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.file_storage.model.entity.UserFileMetadata;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private UserFileMetadata userFileMetadata;

    @Column(columnDefinition = "TEXT")
    private String about;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String instagramLink;
    private String githubLink;

    @OneToOne(mappedBy = "userInfo")
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}

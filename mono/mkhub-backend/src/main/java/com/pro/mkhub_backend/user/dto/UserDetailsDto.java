package com.pro.mkhub_backend.user.dto;

import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.auth.model.enums.Role;
import com.pro.mkhub_backend.file_storage.model.entity.UserFileMetadata;
import com.pro.mkhub_backend.file_storage.util.GoogleDriveLinkBuilder;
import com.pro.mkhub_backend.user.model.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
    public class UserDetailsDto {

        private Long id;
        private String about;
        private String firstName;
        private String lastName;
        private String username;
        private Role role;
        private String email;
        private String phone;
        private String instagram;
        private String github;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String imageUrl;

        private boolean enabled;
        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;

    public UserDetailsDto(UserInfo userInfo, User user, GoogleDriveLinkBuilder linkBuilder) {
        this.id = user.getId();
        this.about = userInfo.getAbout();
        this.firstName = userInfo.getFirstName();
        this.lastName = userInfo.getLastName();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.phone = userInfo.getPhoneNumber();
        this.email = userInfo.getEmail();
        this.instagram = userInfo.getInstagramLink();
        this.github = userInfo.getGithubLink();
        this.createdAt = userInfo.getCreatedAt();
        this.updatedAt = userInfo.getUpdatedAt();

        UserFileMetadata avatar = userInfo.getUserFileMetadata();
        if (avatar != null && avatar.getGoogleDriveFileId() != null && !avatar.getGoogleDriveFileId().isEmpty()) {
            this.imageUrl = linkBuilder.buildThumbnailUrl(avatar.getGoogleDriveFileId());
        }

        this.enabled = user.isEnabled();
        this.accountNonExpired = user.isAccountNonExpired();
        this.accountNonLocked = user.isAccountNonLocked();
        this.credentialsNonExpired = user.isCredentialsNonExpired();

    }

}

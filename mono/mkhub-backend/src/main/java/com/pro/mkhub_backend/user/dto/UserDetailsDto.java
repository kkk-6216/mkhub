package com.pro.mkhub_backend.user.dto;

import com.pro.mkhub_backend.auth.model.enums.Role;
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

    public UserDetailsDto(UserInfo userInfo, Role role) {
        this.id = userInfo.getId();
        this.about = userInfo.getAbout();
        this.firstName = userInfo.getFirstName();
        this.lastName = userInfo.getLastName();
        this.username = userInfo.getUsername();
        this.role = role;
        this.email = userInfo.getEmail();
        this.instagram = userInfo.getInstagramLink();
        this.github = userInfo.getGithubLink();
        this.createdAt = userInfo.getCreatedAt();
        this.updatedAt = userInfo.getUpdatedAt();
        this.imageUrl = userInfo.getImageUrl();
    }

}

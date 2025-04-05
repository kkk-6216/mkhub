package com.pro.mkhub_backend.user.dto;

import com.pro.mkhub_backend.user.model.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String instagram;
    private String github;

    public UserData(UserInfo userInfo) {
        this.firstName = userInfo.getFirstName();
        this.lastName = userInfo.getLastName();
        this.email = userInfo.getEmail();
        this.phone = userInfo.getPhoneNumber();
        this.instagram = userInfo.getInstagramLink();
        this.github = userInfo.getGithubLink();
    }
}

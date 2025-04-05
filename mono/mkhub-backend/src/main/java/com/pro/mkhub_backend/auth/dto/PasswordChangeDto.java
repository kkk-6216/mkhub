package com.pro.mkhub_backend.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDto {

    private String username;
    private String oldPassword;

    private String newPassword;
    private String confirmNewPassword;

}

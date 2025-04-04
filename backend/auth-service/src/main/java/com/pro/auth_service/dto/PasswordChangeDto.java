package com.pro.auth_service.dto;

import lombok.Data;

@Data
public class PasswordChangeDto {

    private String username;
    private String oldPassword;

    private String newPassword;
    private String confirmNewPassword;

}

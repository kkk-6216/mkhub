package com.pro.mkhub_backend.user.dto;

import com.pro.mkhub_backend.auth.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationDto {

    private String username;
    private String email;
    private String password;
    private Role role;

}

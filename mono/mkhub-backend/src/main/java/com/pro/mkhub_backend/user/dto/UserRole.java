package com.pro.mkhub_backend.user.dto;

import com.pro.mkhub_backend.auth.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    private Role role;

}

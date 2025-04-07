package com.pro.mkhub_backend.user.dto;

import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.auth.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserItem {

    private Long userId;
    private String username;
    private String email;
    private Role role;
    private boolean enabled;

    public UserItem(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.enabled = user.isEnabled();
        this.email = user.getUserInfo().getEmail();
    }

}

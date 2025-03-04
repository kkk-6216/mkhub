package com.pro.auth_service.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_STUDENT,
    ROLE_MODERATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
package com.pro.auth_service.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    STUDENT,
    MODERATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
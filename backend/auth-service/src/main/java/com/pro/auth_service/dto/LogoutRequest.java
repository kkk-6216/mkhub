package com.pro.auth_service.dto;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}

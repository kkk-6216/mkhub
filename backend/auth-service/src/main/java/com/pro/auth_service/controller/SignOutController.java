package com.pro.auth_service.controller;

import com.pro.auth_service.dto.LogoutRequest;
import com.pro.auth_service.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignOutController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/sign-out")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
        refreshTokenService.deleteByToken(logoutRequest.getRefreshToken());

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Пользователь успешно вышел из системы");
    }

}

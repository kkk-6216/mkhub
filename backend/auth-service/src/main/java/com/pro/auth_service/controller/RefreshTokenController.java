package com.pro.auth_service.controller;

import com.pro.auth_service.dto.AuthResponse;
import com.pro.auth_service.dto.RefreshRequest;
import com.pro.auth_service.model.entity.RefreshToken;
import com.pro.auth_service.model.entity.User;
import com.pro.auth_service.service.RefreshTokenService;
import com.pro.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) {
        String requestToken = request.getRefreshToken();

        if (!jwtUtil.validateRefreshToken(requestToken)) {
            return ResponseEntity.status(401).body("Неверный refresh token");
        }

        Optional<RefreshToken> optionalToken = refreshTokenService.findByToken(requestToken);
        if (optionalToken.isEmpty()) {
            return ResponseEntity.status(401).body("Refresh token не найден");
        }

        RefreshToken refreshTokenEntity = optionalToken.get();
        String username = jwtUtil.getUsernameFromRefreshToken(requestToken);
        String role = jwtUtil.getRoleFromRefreshToken(requestToken);
        String newAccessToken = jwtUtil.generateAccessToken(username, role);

        if (refreshTokenService.isRefreshTokenExpired(refreshTokenEntity)) {
            User user = refreshTokenEntity.getUser();
            RefreshToken newRefreshTokenEntity = refreshTokenService.createRefreshToken(user);
            String newRefreshToken = newRefreshTokenEntity.getToken();
            AuthResponse response = new AuthResponse(newAccessToken, newRefreshToken);
            return ResponseEntity.ok(response);
        } else {
            AuthResponse response = new AuthResponse(newAccessToken, null);
            return ResponseEntity.ok(response);
        }

    }

}

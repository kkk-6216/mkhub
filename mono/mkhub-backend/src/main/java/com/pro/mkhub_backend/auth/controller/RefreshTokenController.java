package com.pro.mkhub_backend.auth.controller;

import com.pro.mkhub_backend.auth.dto.AuthResponse;
import com.pro.mkhub_backend.auth.dto.RefreshRequest;
import com.pro.mkhub_backend.auth.model.entity.RefreshToken;
import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.auth.service.RefreshTokenService;
import com.pro.mkhub_backend.security.utils.JwtUtil;
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
        System.out.println("Refresh token: " + request.getRefreshToken());
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
        Long id = jwtUtil.getIdFromRefreshToken(requestToken);

        String newAccessToken = jwtUtil.generateAccessToken(id, username, role);

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

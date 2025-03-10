package com.pro.auth_service.controller;

import com.pro.auth_service.dto.UserInfoDto;
import com.pro.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthValidationController {

    private final JwtUtil jwtUtil;

    @GetMapping("/validate")
    public ResponseEntity<UserInfoDto> validateToken(@RequestHeader("Authorization") String token) {

        String accessToken = token.replace("Bearer ", "");

        if (!jwtUtil.validateAccessToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserInfoDto userInfo = new UserInfoDto(
                jwtUtil.getIdFromAccessToken(accessToken),
                jwtUtil.getUsernameFromAccessToken(accessToken),
                jwtUtil.getRoleFromAccessToken(accessToken),
                jwtUtil.getExpirationDateFromAccessToken(accessToken)
        );

        return ResponseEntity.ok(userInfo);
    }
}


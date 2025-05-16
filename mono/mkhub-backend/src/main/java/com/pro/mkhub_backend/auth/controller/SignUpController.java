package com.pro.mkhub_backend.auth.controller;

import com.pro.mkhub_backend.auth.dto.AuthResponse;
import com.pro.mkhub_backend.auth.dto.RegistrationRequest;
import com.pro.mkhub_backend.auth.model.entity.RefreshToken;
import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.auth.service.RefreshTokenService;
import com.pro.mkhub_backend.auth.service.UserService;
import com.pro.mkhub_backend.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/sign-up")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {

        if (userService.findByUsername(registrationRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Имя пользователя уже используется");
        }

        User user = userService.createUser(registrationRequest);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registrationRequest.getUsername(),
                        registrationRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRole().name());
        RefreshToken refreshTokenEntity = refreshTokenService.createRefreshToken(user);
        String refreshToken = refreshTokenEntity.getToken();

        AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);

        return ResponseEntity.ok(authResponse);
    }

}

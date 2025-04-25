package com.pro.mkhub_backend.auth.controller;

import com.pro.mkhub_backend.auth.dto.AuthResponse;
import com.pro.mkhub_backend.auth.dto.LoginRequest;
import com.pro.mkhub_backend.auth.model.entity.RefreshToken;
import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.auth.model.enums.Role;
import com.pro.mkhub_backend.auth.service.RefreshTokenService;
import com.pro.mkhub_backend.auth.service.UserService;
import com.pro.mkhub_backend.security.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SignInControllerTest {

    private AuthenticationManager authenticationManager;
    private RefreshTokenService refreshTokenService;
    private UserService userService;
    private JwtUtil jwtUtil;
    private SignInController signInController;

    @BeforeEach
    public void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        refreshTokenService = mock(RefreshTokenService.class);
        userService = mock(UserService.class);
        jwtUtil = mock(JwtUtil.class);

        signInController = new SignInController(authenticationManager, refreshTokenService, userService, jwtUtil);
    }

    @Test
    public void testLogin_Success() {
        String username = "testuser";
        String password = "password";
        String accessToken = "access.token.example";
        String refreshToken = "refresh.token.example";

        LoginRequest loginRequest = new LoginRequest(username, password);

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(username);
        mockUser.setRole(Role.ROLE_STUDENT);

        when(userService.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateAccessToken(mockUser.getId(), username, Role.ROLE_STUDENT.name()))
                .thenReturn(accessToken);
        when(refreshTokenService.createRefreshToken(mockUser))
                .thenReturn(new RefreshToken(1L, refreshToken, null, mockUser));

        ResponseEntity<?> responseEntity = signInController.login(loginRequest);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertInstanceOf(AuthResponse.class, responseEntity.getBody());

        AuthResponse response = (AuthResponse) responseEntity.getBody();
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertEquals(mockAuth, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testLogin_UserNotFound() {
        String username = "not_found";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(username, password);

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                signInController.login(loginRequest));

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(jwtUtil, never()).generateAccessToken(any(), any(), any());
    }

    @Test
    public void testLogin_InvalidCredentials() {
        String username = "wrong";
        String password = "wrong";
        LoginRequest loginRequest = new LoginRequest(username, password);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        BadCredentialsException ex = assertThrows(BadCredentialsException.class, () ->
                signInController.login(loginRequest));

        assertEquals("Bad credentials", ex.getMessage());
        verify(userService, never()).findByUsername(any());
    }

    @Test
    public void testLogin_TokenGenerationFails() {
        String username = "testuser";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(username, password);

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(username);
        mockUser.setRole(Role.ROLE_STUDENT);

        when(userService.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateAccessToken(any(), any(), any()))
                .thenThrow(new RuntimeException("JWT generation failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                signInController.login(loginRequest));

        assertEquals("JWT generation failed", exception.getMessage());
        verify(refreshTokenService, never()).createRefreshToken(any());
    }

}

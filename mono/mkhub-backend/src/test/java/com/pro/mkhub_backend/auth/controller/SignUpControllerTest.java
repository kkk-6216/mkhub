package com.pro.mkhub_backend.auth.controller;

import com.pro.mkhub_backend.auth.dto.AuthResponse;
import com.pro.mkhub_backend.auth.dto.RegistrationRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SignUpControllerTest {

    private SignUpController signUpController;
    private UserService userService;
    private JwtUtil jwtUtil;
    private RefreshTokenService refreshTokenService;
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        refreshTokenService = mock(RefreshTokenService.class);
        userService = mock(UserService.class);
        jwtUtil = mock(JwtUtil.class);

        signUpController = new SignUpController(userService, jwtUtil, refreshTokenService, authenticationManager);
    }

    @Test
    public void testRegister_SuccessfulRegistration() {

        String username = "newUser";
        String password = "password123";
        String email = "email@example.com";
        String accessToken = "access.token.example";
        String refreshToken = "refresh.token.example";

        RegistrationRequest registrationRequest = new RegistrationRequest(username, password, email);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(username);
        mockUser.setPassword(password);
        mockUser.setRole(Role.ROLE_STUDENT);

        when(userService.findByUsername(username)).thenReturn(Optional.empty());
        when(userService.createUser(registrationRequest)).thenReturn(mockUser);

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);

        when(jwtUtil.generateAccessToken(mockUser.getId(), mockUser.getUsername(), mockUser.getRole().name()))
                .thenReturn(accessToken);
        when(refreshTokenService.createRefreshToken(mockUser))
                .thenReturn(new RefreshToken(1L, refreshToken, null, mockUser));

        ResponseEntity<?> responseEntity = signUpController.register(registrationRequest);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertInstanceOf(AuthResponse.class, responseEntity.getBody());

        AuthResponse response = (AuthResponse) responseEntity.getBody();
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testRegister_UserAlreadyExists() {
        String username = "existingUser";
        String password = "password123";
        String email = "email@example.com";
        RegistrationRequest registrationRequest = new RegistrationRequest(username, password, email);

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(password);

        when(userService.findByUsername(username)).thenReturn(Optional.of(mockUser));

        ResponseEntity<?> response = signUpController.register(registrationRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Имя пользователя уже используется", response.getBody());
        verify(userService, never()).createUser((RegistrationRequest) any());
    }

    @Test
    public void testRegister_TokenGenerationFails() {
        String username = "newUser";
        String password = "password123";
        String email = "email@example.com";
        RegistrationRequest registrationRequest = new RegistrationRequest(username, password, email);

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(password);
        mockUser.setRole(Role.ROLE_STUDENT);

        when(userService.findByUsername(username)).thenReturn(Optional.empty());
        when(userService.createUser(registrationRequest)).thenReturn(mockUser);

        when(jwtUtil.generateAccessToken(mockUser.getId(), mockUser.getUsername(), mockUser.getRole().name()))
                .thenThrow(new RuntimeException("JWT generation failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> signUpController.register(registrationRequest));

        assertEquals("JWT generation failed", exception.getMessage());
        verify(refreshTokenService, never()).createRefreshToken(any());
    }

}

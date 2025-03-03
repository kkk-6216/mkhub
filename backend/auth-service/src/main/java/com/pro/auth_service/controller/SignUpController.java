package com.pro.auth_service.controller;

import com.pro.auth_service.dto.RegistrationRequest;
import com.pro.auth_service.model.entity.User;
import com.pro.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {

        if (userService.findByUsername(registrationRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        userService.createUser(registrationRequest);

        return ResponseEntity.ok("User registered successfully");
    }

}

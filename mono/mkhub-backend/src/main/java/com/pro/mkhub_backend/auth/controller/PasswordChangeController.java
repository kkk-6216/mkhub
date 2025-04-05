package com.pro.mkhub_backend.auth.controller;

import com.pro.mkhub_backend.auth.dto.PasswordChangeDto;
import com.pro.mkhub_backend.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/change-password")
@RequiredArgsConstructor
public class PasswordChangeController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDto passwordChangeDto) {
        System.out.println(passwordChangeDto);
        userService.changePassword(passwordChangeDto);
        return ResponseEntity.ok("Пароль успешно изменён");
    }

}

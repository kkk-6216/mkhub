package com.pro.mkhub_backend.user.controller;

import com.pro.mkhub_backend.auth.service.UserService;
import com.pro.mkhub_backend.security.model.AuthenticatedUserDto;
import com.pro.mkhub_backend.user.dto.UserAbout;
import com.pro.mkhub_backend.user.dto.UserData;
import com.pro.mkhub_backend.user.dto.UserDetailsDto;
import com.pro.mkhub_backend.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserService userService;

    @PatchMapping("/about")
    public ResponseEntity<String> updateUserInfo(@AuthenticationPrincipal AuthenticatedUserDto authenticatedUserDto, @RequestBody UserAbout userAbout) {
        Long userId = userService.findById(authenticatedUserDto.id()).getUserInfo().getId();
        userInfoService.updateAbout(userAbout, userId);
        return ResponseEntity.ok("Updating user with id: " + authenticatedUserDto.id() + " successfully");
    }

    @PatchMapping("/details")
    public ResponseEntity<String> updateUserInfo(@AuthenticationPrincipal AuthenticatedUserDto authenticatedUserDto, @RequestBody UserData userData) {
        Long userId = userService.findById(authenticatedUserDto.id()).getUserInfo().getId();
        userInfoService.updateData(userData, userId);
        return ResponseEntity.ok("Updating user with id: " + authenticatedUserDto.id() + " successfully");
    }

    @GetMapping("/about")
    public ResponseEntity<UserAbout> getUserAbout(@AuthenticationPrincipal AuthenticatedUserDto authenticatedUserDto) {
        Long userId = userService.findById(authenticatedUserDto.id()).getUserInfo().getId();
        return ResponseEntity.ok(userInfoService.getUserAboutById(userId));
    }

    @GetMapping("/details")
    public ResponseEntity<UserData> getUserData(@AuthenticationPrincipal AuthenticatedUserDto authenticatedUserDto) {
        Long userId = userService.findById(authenticatedUserDto.id()).getUserInfo().getId();
        return ResponseEntity.ok(userInfoService.getUserDataById(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable("id") Long id, @AuthenticationPrincipal Object user) {
        System.out.println(user);
        Long userId = userService.findById(id).getUserInfo().getId();
        return ResponseEntity.ok(userInfoService.getUserDetailsById(userId));
    }

}

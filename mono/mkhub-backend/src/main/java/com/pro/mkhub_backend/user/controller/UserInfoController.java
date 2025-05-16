package com.pro.mkhub_backend.user.controller;

import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.auth.service.UserService;
import com.pro.mkhub_backend.security.model.AuthenticatedUserDto;
import com.pro.mkhub_backend.user.dto.*;
import com.pro.mkhub_backend.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userInfoService.getUserDetailsById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserItem>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        System.out.println("1");
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

//    -----------------------------------------------------------------------------------------------------------
    @PatchMapping("/{id}/username")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUsername(@PathVariable("id") Long id, @RequestBody UserUsername userUsername) {
        System.out.println("here: " + userUsername.getUsername());
        userService.updateUsername(id, userUsername.getUsername());
        System.out.println("success");
        return ResponseEntity.ok("Updating user with id: " + id + " successfully");
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRole(@PathVariable("id") Long id, @RequestBody UserRole userRole) {
        userService.updateUserRole(id, userRole.getRole());
        return ResponseEntity.ok("Updating user with id: " + id + " successfully");
    }

    @PatchMapping("/{id}/data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateData(@PathVariable("id") Long id, @RequestBody UserData userData) {
        Long userId = userService.findById(id).getUserInfo().getId();
        userInfoService.updateData(userData, userId);
        return ResponseEntity.ok("Updating user with id: " + id + " successfully");
    }

    @PatchMapping("/{id}/about")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAbout(@PathVariable("id") Long id, @RequestBody UserAbout userAbout) {
        Long userId = userService.findById(id).getUserInfo().getId();
        userInfoService.updateAbout(userAbout, userId);
        return ResponseEntity.ok("Updating user with id: " + id + " successfully");
    }

    @PatchMapping("/{id}/secure-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSecureStatus(@PathVariable("id") Long id, @RequestBody SecureStatus secureStatus) {
        userService.updateSecureStatus(id, secureStatus);
        return ResponseEntity.ok("Updating user with id: " + id + " successfully");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSecureStatus(@RequestBody UserCreationDto userCreationDto) {
        if (userService.findByUsername(userCreationDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Имя пользователя уже используется");
        }
        User user = userService.createUser(userCreationDto);
        return ResponseEntity.ok("Created user with id: " + user.getId() + " successfully");
    }

}

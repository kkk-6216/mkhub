package com.pro.mkhub_backend.file_storage.controller;

import com.pro.mkhub_backend.auth.service.UserService;
import com.pro.mkhub_backend.file_storage.dto.UserAvatarDto;
import com.pro.mkhub_backend.file_storage.model.entity.UserFileMetadata;
import com.pro.mkhub_backend.file_storage.service.UserFileService;
import com.pro.mkhub_backend.security.model.AuthenticatedUserDto;
import com.pro.mkhub_backend.user.model.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class UserFileController {

    private final UserFileService userFileService;
    private final UserService userService;

    @PostMapping("/avatar")
    public ResponseEntity<UserAvatarDto> uploadAvatar(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal AuthenticatedUserDto authentication) {

        UserInfo userInfo = userService.findById(authentication.id()).getUserInfo();

        UserFileMetadata metadata = userFileService.uploadUserAvatar(userInfo.getId(), file);
        UserAvatarDto dto = convertToAvatarDto(metadata);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/avatar/link")
    public ResponseEntity<UserAvatarDto> getAvatarPublicLink(@AuthenticationPrincipal AuthenticatedUserDto authentication) {

        UserInfo userInfo = userService.findById(authentication.id()).getUserInfo();

        UserFileMetadata metadata = userFileService.getUserAvatar(userInfo.getId());

        UserAvatarDto userAvatarDto = convertToAvatarDto(metadata);
        return ResponseEntity.ok(userAvatarDto);
    }

    @PutMapping("/avatar")
    public ResponseEntity<UserAvatarDto> updateAvatar(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal AuthenticatedUserDto authentication) {
        UserInfo userInfo = userService.findById(authentication.id()).getUserInfo();
        Long userId = userInfo.getId();

        UserFileMetadata updatedAvatar = userFileService.updateUserAvatar(userId, file);
        UserAvatarDto dto = convertToAvatarDto(updatedAvatar);
        return ResponseEntity.ok(dto);
    }


    private UserAvatarDto convertToAvatarDto(UserFileMetadata metadata) {
        UserAvatarDto dto = new UserAvatarDto();
        dto.setId(metadata.getId());
        dto.setImageUrl("https://drive.google.com/thumbnail?id=" + metadata.getGoogleDriveFileId());
        return dto;
    }

}
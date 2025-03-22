package com.pro.file_storage_service.controller;

import com.pro.file_storage_service.dto.UserAvatarDto;
import com.pro.file_storage_service.model.entity.UserFileMetadata;
import com.pro.file_storage_service.service.UserFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class UserFileController {

    private final UserFileService userFileService;

    @PostMapping("/avatar")
    public ResponseEntity<UserAvatarDto> uploadAvatar(@RequestParam("file") MultipartFile file) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = (Long) authentication.getPrincipal();

        UserFileMetadata metadata = userFileService.uploadUserAvatar(userId, file);
        UserAvatarDto dto = convertToAvatarDto(metadata);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/avatar/link")
    public ResponseEntity<UserAvatarDto> getAvatarPublicLink() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        UserFileMetadata metadata = userFileService.getUserAvatar(userId);

        UserAvatarDto userAvatarDto = convertToAvatarDto(metadata);
        return ResponseEntity.ok(userAvatarDto);
    }

    @PutMapping("/avatar")
    public ResponseEntity<UserAvatarDto> updateAvatar(@RequestParam("file") MultipartFile file) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

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
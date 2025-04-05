package com.pro.mkhub_backend.file_storage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAvatarDto {

    private Long id;
    private String imageUrl;

}

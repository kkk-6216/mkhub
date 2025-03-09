package com.pro.file_storage_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfoDto {

    private Long id;
    private String username;
    private String role;

}

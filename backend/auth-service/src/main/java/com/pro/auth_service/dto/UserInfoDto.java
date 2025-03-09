package com.pro.auth_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private Long id;
    private String username;
    private String role;

}

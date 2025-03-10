package com.pro.auth_service.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private Long id;
    private String username;
    private String role;
    private Date tokenExpirationAt;

}

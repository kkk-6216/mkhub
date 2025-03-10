package com.pro.file_storage_service.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfoDto {

    private Long id;
    private String username;
    private String role;
    private Date tokenExpirationAt;

}

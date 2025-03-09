package com.pro.auth_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    private String username;
    private String password;
    private String email;

}

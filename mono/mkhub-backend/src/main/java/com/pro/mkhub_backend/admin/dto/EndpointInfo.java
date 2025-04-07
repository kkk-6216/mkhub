package com.pro.mkhub_backend.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointInfo {

    private String url;
    private String method;
    private List<String> roles;

}

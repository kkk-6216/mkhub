package com.pro.mkhub_backend.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry {
    private String timestamp;
    private String user;
    private String role;
    private String method;
    private String url;
    private String duration;
}


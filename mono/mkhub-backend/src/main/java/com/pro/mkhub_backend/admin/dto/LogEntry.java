package com.pro.mkhub_backend.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry {

    private LocalDateTime timestamp;
    private String user;
    private String role;
    private String method;
    private String url;
    private String status;
    private long duration;

    private String requestBody;
    private String responseBody;

    private String authorization;
    private String contentType;
    private String userAgent;
    private String ipAddress;
}

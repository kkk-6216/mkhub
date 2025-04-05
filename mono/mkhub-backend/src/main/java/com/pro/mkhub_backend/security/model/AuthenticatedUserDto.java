package com.pro.mkhub_backend.security.model;

public record AuthenticatedUserDto(Long id, String username, String role) {}

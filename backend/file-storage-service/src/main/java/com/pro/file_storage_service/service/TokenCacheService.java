package com.pro.file_storage_service.service;

import com.pro.file_storage_service.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenCacheService {

    private final RedisTemplate<String, UserInfoDto> redisTemplate;

    public void cacheToken(String token, UserInfoDto user) {
        Instant issuedAtInstant = user.getTokenExpirationAt().toInstant();
        Instant now = Instant.now();

        Duration duration = Duration.between(issuedAtInstant, now);
        String key = "jwt:" + token;
        redisTemplate.opsForValue().set(key, user, duration);
    }

    public boolean isTokenCached(String token) {
        String key = "jwt:" + token;
        UserInfoDto isExists = redisTemplate.opsForValue().get(key);
        return isExists != null;
    }

    public UserInfoDto getValue(String token) {
        String key = "jwt:" + token;
        return redisTemplate.opsForValue().get(key);
    }


}


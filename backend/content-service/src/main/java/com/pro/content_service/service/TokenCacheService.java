package com.pro.content_service.service;

import com.pro.content_service.dto.user.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenCacheService {

    private final RedisTemplate<String, UserInfoDto> redisTemplate;

    public void cacheToken(String token, UserInfoDto user) {

        Instant issuedAtInstant = user.getTokenExpirationAt().toInstant();
        Instant now = Instant.now();
        Duration duration = Duration.between(now, issuedAtInstant);
        if (duration.isNegative() || duration.isZero()) {
            duration = Duration.ofMinutes(10);
        }

        String key = "jwt:" + token;

        redisTemplate.opsForValue().set(key, user, duration.getSeconds(), TimeUnit.SECONDS);
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


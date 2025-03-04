package com.pro.auth_service.service;

import com.pro.auth_service.model.entity.RefreshToken;
import com.pro.auth_service.model.entity.User;
import com.pro.auth_service.repository.RefreshTokenRepository;
import com.pro.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        String token = jwtUtil.generateRefreshToken(user.getUsername(), user.getRole().name());
        Date expiryDate = new Date(new Date().getTime() + jwtUtil.getRefreshTokenExpiration());

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken(token, expiryDate, user);
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isRefreshTokenExpired(RefreshToken token) {
        return token.getExpiryDate().before(new Date());
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteByToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
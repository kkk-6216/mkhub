package com.pro.mkhub_backend.auth.service;

import com.pro.mkhub_backend.auth.model.entity.RefreshToken;
import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.auth.repository.RefreshTokenRepository;
import com.pro.mkhub_backend.security.utils.JwtUtil;
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

        String token = jwtUtil.generateRefreshToken(user.getId(), user.getUsername(), user.getRole().name());
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
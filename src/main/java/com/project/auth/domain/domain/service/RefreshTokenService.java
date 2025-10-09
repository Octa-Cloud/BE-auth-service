package com.project.auth.domain.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final static String refreshTokenPrefix = "REFRESH_TOKEN:";
    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(Long userNo, String refreshToken, Duration timeout) {
        redisTemplate.opsForValue().set(refreshTokenPrefix + userNo, refreshToken, timeout);
    }

    public void deleteRefreshToken(Long userNo) {
        redisTemplate.delete(refreshTokenPrefix + userNo);
    }

    public boolean isExist(String token, Long userNo) {
        String savedToken = redisTemplate.opsForValue().get(refreshTokenPrefix + userNo);

        if(savedToken == null)
            return false;

        return Objects.equals(savedToken, token);
    }
}

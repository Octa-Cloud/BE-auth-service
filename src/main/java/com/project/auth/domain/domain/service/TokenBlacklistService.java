package com.project.auth.domain.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    private final static String blacklistPrefix = "BLACKLIST:";

    public boolean isBlacklistToken(String token) {
        String savedToken = redisTemplate.opsForValue().get(blacklistPrefix + token);

        if(savedToken == null)
            return false;

        return Objects.equals(savedToken, token);
    }

    public void blacklist(String token, Duration expiration) {
        redisTemplate.opsForValue().set(blacklistPrefix + token, "", expiration);
    }
}

package com.project.auth.domain.application.usecase;

import com.project.auth.domain.domain.service.RefreshTokenService;
import com.project.auth.domain.domain.service.TokenBlacklistService;
import com.project.auth.global.exception.RestApiException;
import com.project.auth.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.project.auth.global.exception.code.status.AuthErrorStatus.INVALID_ACCESS_TOKEN;
import static com.project.auth.global.exception.code.status.AuthErrorStatus.INVALID_ID_TOKEN;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutUseCase {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;

    public void execute(String accessToken) {
        Long userNo = tokenProvider.getId(accessToken)
                .orElseThrow(() -> new RestApiException(INVALID_ID_TOKEN));

        Duration expiration = tokenProvider.getRemainingDuration(accessToken)
                .orElseThrow(() -> new RestApiException(INVALID_ACCESS_TOKEN));

        refreshTokenService.deleteRefreshToken(userNo);
        tokenBlacklistService.blacklist(accessToken, expiration);
    }
}

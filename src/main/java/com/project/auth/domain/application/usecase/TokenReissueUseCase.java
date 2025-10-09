package com.project.auth.domain.application.usecase;

import com.project.auth.domain.application.dto.response.TokenReissueResponse;
import com.project.auth.domain.domain.service.RefreshTokenService;
import com.project.auth.global.exception.RestApiException;
import com.project.auth.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.project.auth.global.exception.code.status.AuthErrorStatus.EXPIRED_MEMBER_JWT;
import static com.project.auth.global.exception.code.status.AuthErrorStatus.INVALID_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class TokenReissueUseCase {

    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;

    public TokenReissueResponse execute(String refreshToken, Long userNo) {
        if(!refreshTokenService.isExist(refreshToken, userNo))
            throw new RestApiException(INVALID_REFRESH_TOKEN);

        refreshTokenService.deleteRefreshToken(userNo);

        String newAccessToken = tokenProvider.createAccessToken(userNo);
        String newRefreshToken = tokenProvider.createRefreshToken(userNo);

        Duration duration = tokenProvider.getRemainingDuration(refreshToken)
                .orElseThrow(() -> new RestApiException(EXPIRED_MEMBER_JWT));
        refreshTokenService.saveRefreshToken(userNo, newRefreshToken, duration);

        return new TokenReissueResponse(newAccessToken, newRefreshToken);
    }
}

package com.project.auth.domain.application.usecase;

import com.project.auth.domain.application.dto.request.LoginRequest;
import com.project.auth.domain.application.dto.response.LoginResponse;
import com.project.auth.domain.domain.service.RefreshTokenService;
import com.project.auth.domain.infra.client.VerifyUserCredentialsClient;
import com.project.auth.global.exception.RestApiException;
import com.project.auth.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.project.auth.global.exception.code.status.AuthErrorStatus.EXPIRED_MEMBER_JWT;
import static com.project.auth.global.exception.code.status.AuthErrorStatus.LOGIN_ERROR;


@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final VerifyUserCredentialsClient verifyUserCredentialsClient;

    public LoginResponse execute(LoginRequest request) {
        Long userNo = verifyUserCredentialsClient.authenticate(request);

        String accessToken = tokenProvider.createAccessToken(userNo);
        String refreshToken = tokenProvider.createRefreshToken(userNo);

        Duration tokenExpiration = tokenProvider.getRemainingDuration(refreshToken)
                .orElseThrow(() -> new RestApiException(EXPIRED_MEMBER_JWT));
        refreshTokenService.saveRefreshToken(userNo, refreshToken, tokenExpiration);

        return new LoginResponse(accessToken, refreshToken);
    }
}

package com.project.auth.global.interceptor;

import com.project.auth.global.exception.RestApiException;
import com.project.auth.domain.domain.service.TokenBlacklistService;
import com.project.auth.global.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.project.auth.global.exception.code.status.AuthErrorStatus.EMPTY_JWT;
import static com.project.auth.global.exception.code.status.AuthErrorStatus.EXPIRED_MEMBER_JWT;

@Component
@RequiredArgsConstructor
public class JwtBlacklistInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = tokenProvider.getToken(request)
                .orElseThrow(() -> new RestApiException(EMPTY_JWT));

        if (tokenBlacklistService.isBlacklistToken(token)) {
            throw new RestApiException(EXPIRED_MEMBER_JWT);
        }

        return true;
    }
}

package com.project.auth.domain.application.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {}

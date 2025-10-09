package com.project.auth.domain.application.dto.response;

public record TokenReissueResponse (
        String accessToken,
        String refreshToken
) {}

package com.project.auth.domain.ui.controller.spec;

import com.project.auth.domain.application.dto.response.TokenReissueResponse;
import com.project.auth.global.annotation.CurrentUser;
import com.project.auth.global.annotation.RefreshToken;
import com.project.auth.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User")
public interface TokenReissueApiSpec {

    @Operation(
            summary = "토큰 재발급 API",
            description = "만료된 액세스 토큰을 갱신하기 위해 리프레시 토큰을 이용하여 새로운 액세스 토큰과 리프레시 토큰을 발급합니다."
    )
    @PostMapping("/api/auth/reissue")
    BaseResponse<TokenReissueResponse> reissue(
            @Parameter(hidden = true) @CurrentUser Long userNo,
            @Parameter(hidden = true) @RefreshToken String refreshToken
    );
}

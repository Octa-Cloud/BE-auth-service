package com.project.auth.domain.ui.controller.spec;

import com.project.auth.global.annotation.AccessToken;
import com.project.auth.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;

@Tag(name = "User")
public interface LogoutApiSpec {

    @Operation(
            summary = "로그아웃 API",
            description = "현재 로그인된 사용자의 액세스 토큰을 무효화하여 로그아웃 처리합니다."
    )
    @DeleteMapping("/api/auth/logout")
    BaseResponse<Void> logout(
            @Parameter(hidden = true) @AccessToken String accessToken
    );
}

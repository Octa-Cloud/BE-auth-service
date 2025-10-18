package com.project.auth.domain.ui.controller.spec;

import com.project.auth.domain.application.dto.request.LoginRequest;
import com.project.auth.domain.application.dto.response.LoginResponse;
import com.project.auth.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User")
public interface LoginApiSpec {

    @Operation(
            summary = "로그인 API",
            description = "사용자가 이메일/비밀번호를 입력해 로그인하고, "
                    + "JWT 토큰을 포함한 인증 정보를 반환합니다."
    )
    @PostMapping("/api/auth/login")
    BaseResponse<LoginResponse> login(
            @RequestBody(
                    description = "로그인 요청 바디 (이메일, 비밀번호)",
                    required = true
            )
            @org.springframework.web.bind.annotation.RequestBody @Valid
            LoginRequest request
    );
}

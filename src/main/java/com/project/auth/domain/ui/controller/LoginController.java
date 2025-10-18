package com.project.auth.domain.ui.controller;

import com.project.auth.domain.application.dto.request.LoginRequest;
import com.project.auth.domain.application.dto.response.LoginResponse;
import com.project.auth.domain.application.usecase.LoginUseCase;
import com.project.auth.domain.ui.controller.spec.LoginApiSpec;
import com.project.auth.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController implements LoginApiSpec {

    private final LoginUseCase loginUseCase;

    @Override
    public BaseResponse<LoginResponse> login(
            LoginRequest request
    ) {
        return BaseResponse.onSuccess(loginUseCase.execute(request));
    }
}

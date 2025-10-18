package com.project.auth.domain.ui.controller;

import com.project.auth.domain.application.usecase.LogoutUseCase;
import com.project.auth.domain.ui.controller.spec.LogoutApiSpec;
import com.project.auth.global.annotation.AccessToken;
import com.project.auth.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogoutController implements LogoutApiSpec {

    private final LogoutUseCase logoutUseCase;

    @Override
    public BaseResponse<Void> logout(
            String accessToken
    ) {
        logoutUseCase.execute(accessToken);
        return BaseResponse.onSuccess();
    }
}

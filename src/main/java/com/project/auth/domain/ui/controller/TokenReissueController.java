package com.project.auth.domain.ui.controller;

import com.project.auth.domain.application.dto.response.TokenReissueResponse;
import com.project.auth.domain.application.usecase.TokenReissueUseCase;
import com.project.auth.domain.ui.controller.spec.TokenReissueApiSpec;
import com.project.auth.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenReissueController implements TokenReissueApiSpec {

    private final TokenReissueUseCase tokenReissueUseCase;

    @Override
    public BaseResponse<TokenReissueResponse> reissue(
            Long userNo,
            String refreshToken
    ) {
        return BaseResponse.onSuccess(tokenReissueUseCase.execute(refreshToken, userNo));
    }
}

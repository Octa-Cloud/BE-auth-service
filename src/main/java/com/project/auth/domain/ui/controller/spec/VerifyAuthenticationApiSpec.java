package com.project.auth.domain.ui.controller.spec;

import com.project.auth.global.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Auth")
public interface VerifyAuthenticationApiSpec {

    @Operation(
            summary = "인증이 필요한 요청의 인증을 담당하는 API"
    )
    @PostMapping("/api/auth/verify")
    ResponseEntity<Void> verify(
        @CurrentUser Long userNo
    );
}

package com.project.auth.domain.ui.controller;

import com.project.auth.domain.ui.controller.spec.VerifyAuthenticationApiSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyAuthenticationController implements VerifyAuthenticationApiSpec {

    @Override
    public ResponseEntity<Void> verify(
            Long userNo
    ) {
        return ResponseEntity.ok()
//                .header("X-User-Id", userNo.toString()) // todo: userNo Long -> String
                .build();
    }
}

package com.project.auth.global.exception;

import com.project.auth.global.exception.code.BaseCode;
import com.project.auth.global.exception.code.BaseCodeInterface;
import com.project.auth.global.exception.code.status.GlobalErrorStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestApiException extends RuntimeException {

    private final BaseCodeInterface errorCode;

    public BaseCode getErrorCode() {
        return this.errorCode.getCode();
    }
}

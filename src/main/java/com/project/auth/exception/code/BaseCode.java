package com.project.auth.exception.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class BaseCode {
    private final HttpStatus httpStatus;
    private final boolean isSuccess;
    private final String code;
    private final String message;
}
package com.project.auth.global.exception.code.status;

import com.project.auth.global.exception.code.BaseCode;
import com.project.auth.global.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorStatus implements BaseCodeInterface {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON402", "Validation Error입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 정보를 찾을 수 없습니다."),
    _METHOD_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "COMMON405", "Argument Type이 올바르지 않습니다."),
    _CONTAIN_BAD_WORD(HttpStatus.BAD_REQUEST, "COMMON400", "입력하신 내용에 부적절한 단어가 포함되어 있습니다."),
    _EXIST_ENTITY(HttpStatus.BAD_REQUEST, "COMMON400", "이미 존재하는 요청입니다."),
    _TOO_MANY_REQUEST(HttpStatus.TOO_MANY_REQUESTS, "COMMON429", "요청이 너무 많습니다. 잠시 후 다시 시도해 주세요."),
    _PATIENT_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMON400", "존재하지 않는 환자 코드입니다."),

    // S3 관련 에러
    _S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5001", "파일 업로드에 실패했습니다."),
    _S3_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5002", "파일 삭제에 실패했습니다."),
    _FAILED_READ_FILE(HttpStatus.BAD_REQUEST, "FILE002","파일을 읽는 중 문제가 발생하였습니다."),

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "예외처리 테스트입니다."),

    // Sleep Goal
    SLEEP_GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "GOAL4001", "해당 유저의 수면 목표가 존재하지 않습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4001", "해당 유저를 찾을 수 없습니다."),

    // Mail
    FAILED_SEND_VERIFY_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "MAIL001", "인증번호 전송에 실패하였습니다."),

    // Room
    USER_IN_ROOM(HttpStatus.BAD_REQUEST, "ROOM001", "이미 속한 방입니다."),
    DUPLICATE_JOIN_REQUEST(HttpStatus.BAD_REQUEST, "ROOM002", "이미 전송한 요청입니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM003", "존재하지 않는 방입니다."),
    COMPLETED_ROOM_EXISTS(HttpStatus.BAD_REQUEST, "ROOM004", "이미 확정된 방이 존재하는 유저입니다."),
    NO_PERMISSION_ON_ROOM(HttpStatus.UNAUTHORIZED, "ROOM005", "권한이 없습니다."),
    ROOM_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM006", "존재하지 않는 요청입니다."),
    ROOM_IS_NOT_FULL(HttpStatus.BAD_REQUEST, "ROOM007", "방이 가득차지 않았습니다."),
    ALREADY_CONFIRM_REQUEST(HttpStatus.BAD_REQUEST, "ROOM008", "이미 처리된 요청입니다."),

    // Password
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST,"PASSWORD001","비밀번호와 확인 비밀번호가 일치하지 않습니다.")
    ;
    private final HttpStatus httpStatus;
    private final boolean isSuccess = false;
    private final String code;
    private final String message;

    public BaseCode getCode() {
        return BaseCode.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}


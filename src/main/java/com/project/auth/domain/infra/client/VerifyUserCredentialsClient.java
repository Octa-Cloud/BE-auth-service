package com.project.auth.domain.infra.client;

import com.project.auth.domain.application.dto.request.LoginRequest;
import com.project.auth.global.common.BaseResponse;
import com.project.auth.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static com.project.auth.global.exception.code.status.AuthErrorStatus.LOGIN_ERROR;
import static com.project.auth.global.exception.code.status.GlobalErrorStatus._INTERNAL_SERVER_ERROR;

@Component
@RequiredArgsConstructor
public class VerifyUserCredentialsClient {

    private final RestClient restClient;
    private static final String VERIFY_URI = "/api/users/internal/credentials";
    private static final ParameterizedTypeReference<BaseResponse<Long>> RESPONSE_TYPE =
            new ParameterizedTypeReference<>() {};

    public Long authenticate(LoginRequest request) {
        BaseResponse<Long> res = restClient.post()
                .uri(VERIFY_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, resp) -> { throw new RestApiException(LOGIN_ERROR); })
                .onStatus(HttpStatusCode::is5xxServerError, (req, resp) -> { throw new RestApiException(_INTERNAL_SERVER_ERROR); })
                .body(RESPONSE_TYPE);

        return Objects.requireNonNull(res).getResult();
    }
}
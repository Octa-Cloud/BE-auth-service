package com.project.auth.global.resolver;

import com.project.auth.global.annotation.CurrentUser;
import com.project.auth.global.exception.RestApiException;
import com.project.auth.global.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.project.auth.global.exception.code.status.GlobalErrorStatus._UNAUTHORIZED;

@Slf4j
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null
                && Long.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Long resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            log.error("DEBUG: CurrentUserArgumentResolver - request is null");
            throw new RestApiException(_UNAUTHORIZED);
        }

        log.debug("DEBUG: CurrentUserArgumentResolver - Authorization header: {}", request.getHeader("Authorization"));
        
        String token = tokenProvider.getToken(request)
                .orElseThrow(() -> {
                    log.error("DEBUG: CurrentUserArgumentResolver - token is null or empty");
                    return new RestApiException(_UNAUTHORIZED);
                });

        log.debug("DEBUG: CurrentUserArgumentResolver - extracted token: {}", token);

        try {
            Long userId = tokenProvider.getId(token)
                    .orElseThrow(() -> {
                        log.error("DEBUG: CurrentUserArgumentResolver - failed to get user ID from token");
                        return new RestApiException(_UNAUTHORIZED);
                    });
            log.debug("DEBUG: CurrentUserArgumentResolver - user ID: {}", userId);
            return userId;
        } catch (Exception e) {
            log.error("DEBUG: CurrentUserArgumentResolver - exception occurred: {}", e.getMessage(), e);
            throw new RestApiException(_UNAUTHORIZED);
        }
    }
}

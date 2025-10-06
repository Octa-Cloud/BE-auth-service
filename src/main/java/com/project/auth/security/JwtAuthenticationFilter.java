package com.project.auth.security;

import com.project.auth.exception.RestApiException;
import com.project.auth.properties.ExcludeAuthPathProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.io.PrintWriter;

import static com.project.auth.exception.code.status.AuthErrorStatus.EMPTY_JWT;
import static com.project.auth.exception.code.status.AuthErrorStatus.INVALID_ACCESS_TOKEN;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ExcludeAuthPathProperties excludeAuthPathProperties;
    private static final PathPatternParser pathPatternParser = new PathPatternParser();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isExcludedPath(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = tokenProvider.getToken(request)
                    .orElseThrow(() -> new RestApiException(EMPTY_JWT));

            // 토큰 검증
            if(tokenProvider.validateToken(token))
                setAuthentication(token);
            else
                throw new RestApiException(INVALID_ACCESS_TOKEN);

            filterChain.doFilter(request, response);
        } catch (RestApiException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            String jsonResponse = String.format("{\"message\": \"%s\"}", e.getMessage());

            PrintWriter writer = response.getWriter();
            writer.write(jsonResponse);
            writer.flush();
            writer.close();
        }
    }

    public boolean isExcludedPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        HttpMethod requestMethod = HttpMethod.valueOf(request.getMethod());

        return excludeAuthPathProperties.getPaths().stream()
                .anyMatch(authPath ->
                        pathPatternParser.parse(authPath.getPathPattern())
                                .matches(PathContainer.parsePath(requestPath))
                        && requestMethod.equals(HttpMethod.valueOf(authPath.getMethod()))
                );
    }

    private void setAuthentication(String token) {
        if (tokenProvider.validateToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}

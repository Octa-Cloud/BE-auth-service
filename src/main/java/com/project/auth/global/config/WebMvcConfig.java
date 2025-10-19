package com.project.auth.global.config;

import com.project.auth.global.interceptor.JwtBlacklistInterceptor;
import com.project.auth.global.properties.ExcludeBlacklistPathProperties;
import com.project.auth.global.resolver.AccessTokenArgumentResolver;
import com.project.auth.global.resolver.CurrentUserArgumentResolver;
import com.project.auth.global.resolver.RefreshTokenArgumentResolver;
import com.project.auth.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenProvider tokenProvider;
    private final JwtBlacklistInterceptor jwtBlacklistInterceptor;
    private final ExcludeBlacklistPathProperties excludeBlacklistPathProperties;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(List.of(
                new CurrentUserArgumentResolver(tokenProvider),
                new RefreshTokenArgumentResolver(tokenProvider),
                new AccessTokenArgumentResolver(tokenProvider)
        ));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtBlacklistInterceptor)
                .excludePathPatterns(excludeBlacklistPathProperties.getExcludeAuthPaths());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:5173",
                    "https://www.mong.live",
                    "http://www.mong.live"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                .allowedHeaders(
                    "Authorization",
                    "Content-Type",
                    "X-Requested-With",
                    "Accept",
                    "Origin",
                    "Access-Control-Request-Method",
                    "Access-Control-Request-Headers",
                    "X-User-Id",
                    "Cache-Control",
                    "Pragma"
                )
                .exposedHeaders(
                    "Authorization",
                    "Content-Type",
                    "X-User-Id",
                    "Access-Control-Allow-Origin",
                    "Access-Control-Allow-Credentials"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }
}

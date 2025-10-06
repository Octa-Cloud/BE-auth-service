package com.project.auth.config;

import com.project.auth.interceptor.JwtBlacklistInterceptor;
import com.project.auth.properties.ExcludeBlacklistPathProperties;
import com.project.auth.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtBlacklistInterceptor jwtBlacklistInterceptor;
    private final ExcludeBlacklistPathProperties excludeBlacklistPathProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtBlacklistInterceptor)
                .excludePathPatterns(excludeBlacklistPathProperties.getExcludeAuthPaths());
        }
}

package com.project.auth.global.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties("jwt-properties")
public class JwtProperties {

    private final String key;
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;
    private final String accessTokenSubject;
    private final String refreshTokenSubject;
    private final String tokenHeader;
    private final String bearer;
    private final String id;

}

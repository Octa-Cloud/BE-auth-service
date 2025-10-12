package com.project.auth.global.security;

import com.project.auth.global.exception.RestApiException;
import com.project.auth.global.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static com.project.auth.global.exception.code.status.AuthErrorStatus.UNSUPPORTED_JWT;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String createAccessToken(Long id) {
        return createToken(id, jwtProperties.getAccessTokenSubject(), jwtProperties.getAccessTokenExpiration());
    }

    public String createRefreshToken(Long id) {
        return createToken(id, jwtProperties.getRefreshTokenSubject(), jwtProperties.getRefreshTokenExpiration());
    }

    private String createToken(Long id, String subject, Long expiration) {
        LocalDateTime now = LocalDateTime.now();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(
                        now.plusMinutes(expiration)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                ))
                .setSubject(subject)
                .claim(jwtProperties.getId(), id)
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new User(String.valueOf(claims.get(jwtProperties.getId(), Long.class)), "", authorities), token, authorities);
    }

    public Optional<Long> getId(String token) {
        try {
            return Optional.ofNullable(getClaims(token).get(jwtProperties.getId(), Long.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<String> getToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getTokenHeader()))
                .filter(token -> token.startsWith(jwtProperties.getBearer()))
                .map(token -> token.replace(jwtProperties.getBearer() + " ", ""));
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Optional<Date> getExpiration(String token) {
        try {
            return Optional.ofNullable(getClaims(token).getExpiration());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Duration> getRemainingDuration(String token) {
        return getExpiration(token)
                .map(date -> Duration.between(Instant.now(), date.toInstant()));
    }

    public boolean isAccessToken(String token) {
        try {
            String subject = getClaims(token).getSubject();
            return jwtProperties.getAccessTokenSubject().equals(subject);
        } catch (Exception e) {
            throw new RestApiException(UNSUPPORTED_JWT);
        }
    }
}

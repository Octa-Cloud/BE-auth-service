// com.project.auth.domain.infra.kafka.AuthRevokeListener.java
package com.project.auth.domain.infra.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.auth.domain.domain.service.TokenBlacklistService;
import com.project.auth.domain.domain.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthRevokeListener {

    private final ObjectMapper om;
    private final TokenBlacklistService blacklist;
    private final RefreshTokenService refreshTokens;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            autoCreateTopics = "false",
            dltTopicSuffix = ".dlt",
            exclude = { InvalidPayloadException.class } // 독성 → 즉시 DLT
    )
    @KafkaListener(
            topics = "auth.token-delete.command",
            groupId = "auth-service",
            containerFactory = "kafkaManualAckFactory"
    )
    public void onRevoke(ConsumerRecord<String, String> rec, Acknowledgment ack) throws Exception {
        final JsonNode n;
        try {
            n = om.readTree(rec.value());
        } catch (Exception e) {
            log.error("[auth] toxic payload -> DLT, value={}", rec.value());
            throw new InvalidPayloadException("Invalid JSON: " + e.getMessage(), e);
        }

        long userNo = n.path("userNo").asLong(0L);
        String accessToken = n.path("accessToken").asText(null);
        long ttlSec = n.path("blacklistSeconds").asLong(0L);

        if (userNo <= 0L) {
            log.error("[auth] toxic payload -> DLT (invalid userNo), value={}", rec.value());
            throw new InvalidPayloadException("Missing/invalid userNo");
        }

        // 1) 액세스 토큰 블랙리스트: 남은 TTL이 있을 때만
        if (accessToken != null && !accessToken.isBlank() && ttlSec > 0) {
            blacklist.blacklist(accessToken, Duration.ofSeconds(ttlSec));
            log.info("[auth] access token blacklisted userNo={}, ttlSec={}", userNo, ttlSec);
        } else {
            log.info("[auth] skip access-token blacklist (tokenMissingOrTtl<=0) userNo={}, ttlSec={}, tokenPresent={}",
                    userNo, ttlSec, accessToken != null && !accessToken.isBlank());
        }

        // 2) 리프레시 토큰은 ‘항상’ 제거 (재발급 경로 차단)
        refreshTokens.deleteRefreshToken(userNo);

        // 3) 오프셋 커밋
        ack.acknowledge();
    }

}
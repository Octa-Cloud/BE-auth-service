package com.project.auth.global.config;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {

    @Bean
    public RestClient userRestClient() {
        return RestClient.builder()
//                .baseUrl("http://localhost:51234")
//                .baseUrl("http://user-service:8080") // 도커용
                .baseUrl("http://user-service.default.svc.cluster.local:8080")
                .requestFactory(new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault()))
                .defaultHeader("X-Internal-Call", "auth-service")
                .build();
    }
}

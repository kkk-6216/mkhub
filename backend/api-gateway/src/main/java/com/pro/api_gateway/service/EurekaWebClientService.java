package com.pro.api_gateway.service;

import com.pro.api_gateway.dto.EurekaResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EurekaWebClientService {

    private final WebClient webClient = WebClient.create("http://localhost:8761");

    public Mono<EurekaResponse> getEurekaApps() {
        return webClient
                .get()
                .uri("/eureka/apps")
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(EurekaResponse.class);
    }
}
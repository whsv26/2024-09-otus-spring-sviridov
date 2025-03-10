package ru.otus.hw.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class RequestIdFilter implements GatewayFilter {

    private static final String HEADER_X_REQUEST_ID = "X-Request-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var uuid = java.util.UUID.randomUUID().toString();
        var request = exchange.getRequest().mutate()
            .header(HEADER_X_REQUEST_ID, uuid)
            .build();

        return chain.filter(exchange.mutate().request(request).build());
    }
}

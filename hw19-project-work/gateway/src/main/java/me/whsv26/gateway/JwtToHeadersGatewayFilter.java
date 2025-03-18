package me.whsv26.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class JwtToHeadersGatewayFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .filter(authentication -> authentication.getPrincipal() instanceof Jwt)
            .map(authentication -> (Jwt) authentication.getPrincipal())
            .map(maybeJwt -> Optional.ofNullable(maybeJwt)
                .flatMap(jwt -> Optional.ofNullable(jwt.getClaimAsString("user_id")))
                .map(userId -> exchange.getRequest().mutate().header("X-User-ID", userId).build())
                .map(request -> exchange.mutate().request(request).build())
                .orElse(exchange)
            )
            .switchIfEmpty(Mono.just(exchange))
            .flatMap(chain::filter);
    }
}

package me.whsv26.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class UserInfoHeadersGatewayFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getPrincipal)
            .filter(principal -> principal instanceof Jwt)
            .map(principal -> (Jwt) principal)
            .map(maybeJwt -> Optional.ofNullable(maybeJwt)
                .map(jwt -> addHeadersFromClaims(jwt, exchange.getRequest()))
                .map(request -> exchange.mutate().request(request).build())
                .orElse(exchange))
            .switchIfEmpty(Mono.just(exchange))
            .flatMap(chain::filter);
    }

    private static ServerHttpRequest addHeadersFromClaims(Jwt jwt, ServerHttpRequest request) {
        var builder = request.mutate();

        Optional.ofNullable(jwt.getClaimAsString("user_id"))
            .filter(userId -> !userId.isEmpty())
            .ifPresent(userId -> builder.header("X-User-ID", userId));

        Optional.ofNullable(jwt.getClaimAsString("authorities"))
            .filter(authorities -> !authorities.isEmpty())
            .ifPresent(authorities -> builder.header("X-User-Authorities", authorities));

        return builder.build();
    }
}

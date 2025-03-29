package me.whsv26.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class PrincipalNameOrIPKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return exchange.getPrincipal()
            .flatMap(principal -> Mono.justOrEmpty(principal.getName()))
            .switchIfEmpty(Mono.justOrEmpty(extractIP(exchange.getRequest())));
    }

    private Optional<String> extractIP(ServerHttpRequest request) {
        return extractFromXForwardedFor(request)
            .or(() -> extractFromRemoteAddress(request));
    }

    private static Optional<String> extractFromXForwardedFor(ServerHttpRequest request) {
        return Optional.ofNullable(request.getHeaders().get("X-Forwarded-For"))
            .filter(s -> !s.isEmpty())
            .flatMap(ips -> ips.stream().findFirst());
    }

    private static Optional<String> extractFromRemoteAddress(ServerHttpRequest request) {
        return Optional
            .ofNullable(request.getRemoteAddress())
            .flatMap(socketAddress -> Optional.ofNullable(socketAddress.getAddress()))
            .flatMap(address -> Optional.ofNullable(address.getHostAddress()));
    }
}

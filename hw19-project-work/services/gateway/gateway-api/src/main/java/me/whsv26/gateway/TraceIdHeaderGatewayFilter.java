package me.whsv26.gateway;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(-1)
public class TraceIdHeaderGatewayFilter implements GatewayFilter {

    private final Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Optional.ofNullable(tracer.currentSpan())
            .map(Span::context)
            .map(TraceContext::traceId)
            .ifPresent(traceId -> writeHeader(exchange, traceId));

        return chain.filter(exchange);
    }

    private static void writeHeader(ServerWebExchange exchange, String traceId) {
        exchange.getResponse()
            .getHeaders()
            .add("X-Trace-Id", traceId);
    }
}

package ru.otus.hw.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.functions.CheckedSupplier;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AuthResilienceInterceptor implements ClientHttpRequestInterceptor {

    private static final String AUTH_BACKEND = "auth";

    private final TimeLimiter timeLimiter;

    private final CircuitBreaker circuitBreaker;

    private final Retry retry;

    private final ScheduledExecutorService scheduler;

    public AuthResilienceInterceptor(
        TimeLimiterRegistry timeLimiterRegistry,
        CircuitBreakerRegistry circuitBreakerRegistry,
        RetryRegistry retryRegistry
    ) {
        this.timeLimiter = timeLimiterRegistry.timeLimiter(AUTH_BACKEND);
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(AUTH_BACKEND);
        this.retry = retryRegistry.retry(AUTH_BACKEND);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public ClientHttpResponse intercept(
        HttpRequest request,
        byte[] body,
        ClientHttpRequestExecution execution
    ) {
        var context = MDC.getCopyOfContextMap();
        CheckedSupplier<ClientHttpResponse> supplier = () -> {
            MDC.setContextMap(context);
            try {
                return execution.execute(request, body);
            } finally {
                MDC.clear();
            }
        };

        return Decorators.ofCompletionStage(() -> CompletableFuture.supplyAsync(supplier.unchecked()))
            .withTimeLimiter(timeLimiter, scheduler)
            .withCircuitBreaker(circuitBreaker)
            .withRetry(retry, scheduler)
            .decorate()
            .get()
            .toCompletableFuture()
            .join();
    }
}

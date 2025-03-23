package me.whsv26.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Configuration
public class AppConfig {

    public static final int RATE_LIMITER_REPLENISH_RATE = 20;

    public static final int RATE_LIMITER_BURST_CAPACITY = 30;

    public static final HttpMethod[] IDEMPOTENT_HTTP_METHODS = {
        HttpMethod.GET,
        HttpMethod.PUT,
        HttpMethod.DELETE
    };

    public static final HttpStatus[] TRANSIENT_HTTP_STATUSES = {
        HttpStatus.REQUEST_TIMEOUT,
        HttpStatus.TOO_MANY_REQUESTS,
        HttpStatus.BAD_GATEWAY,
        HttpStatus.SERVICE_UNAVAILABLE,
        HttpStatus.GATEWAY_TIMEOUT
    };

    @Bean
    RouteLocator gateway(
        RouteLocatorBuilder routeLocatorBuilder,
        AppProps applConfigProperties,
        List<GatewayFilter> filters,
        RedisRateLimiter rateLimiter
    ) {
        var routesBuilder = routeLocatorBuilder.routes();
        for (var route : applConfigProperties.getApiRoutes()) {
            routesBuilder.route(route.id(), routeSpec -> buildRoute(route, routeSpec, filters, rateLimiter));
        }
        return routesBuilder.build();
    }

    private static Buildable<Route> buildRoute(
        ApiRoute route,
        PredicateSpec routeSpec,
        List<GatewayFilter> filters,
        RedisRateLimiter rateLimiter
    ) {
        return routeSpec
            .path(route.prefix() + "/**")
            .filters(filterSpec ->
                filterSpec
                    .circuitBreaker(config -> configureCircuitBreaker(route, config))
                    .retry(config -> configureRetry(route, config))
                    .requestRateLimiter(config -> configureRateLimiter(route, rateLimiter, config))
                    .filters(filters)
                    .rewritePath(route.prefix() + "/(?<segment>.*)", "/api/${segment}")
            )
            .uri(route.uri());
    }

    private static void configureRateLimiter(
        ApiRoute route,
        RedisRateLimiter rateLimiter,
        RequestRateLimiterGatewayFilterFactory.Config config
    ) {
        var rateLimiterConfig = new RedisRateLimiter.Config()
            .setReplenishRate(RATE_LIMITER_REPLENISH_RATE)
            .setBurstCapacity(RATE_LIMITER_BURST_CAPACITY)
            .setRequestedTokens(1);

        rateLimiter.getConfig().put(route.id(), rateLimiterConfig);
        config.setRouteId(route.id());
        config.setRateLimiter(rateLimiter);
        config.setKeyResolver(new PrincipalNameOrIPKeyResolver());
    }

    private static void configureRetry(ApiRoute route, RetryGatewayFilterFactory.RetryConfig config) {
        config.setRouteId(route.id());
        config.setStatuses(TRANSIENT_HTTP_STATUSES);
        config.setMethods(IDEMPOTENT_HTTP_METHODS);
        config.setBackoff(Duration.ofMillis(100), Duration.ofMillis(2000), 2, true);
    }

    private static void configureCircuitBreaker(ApiRoute route, SpringCloudCircuitBreakerFilterFactory.Config config) {
        config.setRouteId(route.id());
        config.setName(route.id());
        config.setStatusCodes(Set.of(HttpStatus.SERVICE_UNAVAILABLE.name()));
    }
}

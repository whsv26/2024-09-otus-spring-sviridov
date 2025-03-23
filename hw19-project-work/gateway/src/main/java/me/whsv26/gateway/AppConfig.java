package me.whsv26.gateway;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerResilience4JFilterFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
@EnableConfigurationProperties(AppProps.class)
public class AppConfig {

    public static final int RATE_LIMITER_REPLENISH_RATE = 20;

    public static final int RATE_LIMITER_BURST_CAPACITY = 30;

    public static final HttpStatus[] TRANSIENT_HTTP_STATUSES = {
        HttpStatus.REQUEST_TIMEOUT,
        HttpStatus.TOO_MANY_REQUESTS,
        HttpStatus.BAD_GATEWAY,
        HttpStatus.SERVICE_UNAVAILABLE,
        HttpStatus.GATEWAY_TIMEOUT
    };

    @Bean
    public RequestIdFilter requestIdFilter() {
        return new RequestIdFilter();
    }

    @Bean
    public JwtToHeadersGatewayFilter jwtToHeadersGatewayFilter() {
        return new JwtToHeadersGatewayFilter();
    }

    @Bean
    RouteLocator gateway(
        RouteLocatorBuilder routeLocatorBuilder,
        AppProps applConfigProperties,
        List<GatewayFilter> commonFilters,
        SpringCloudCircuitBreakerResilience4JFilterFactory circuitBreakerFilterFactory,
        RequestRateLimiterGatewayFilterFactory rateLimiterFilterFactory,
        RetryGatewayFilterFactory retryFilterFactory,
        RedisRateLimiter rateLimiter
    ) {
        var routesBuilder = routeLocatorBuilder.routes();

        for (var route : applConfigProperties.getApiRoutes()) {
            var routeFilters = new ArrayList<>(commonFilters);
            makeRetryFilter(routeFilters, retryFilterFactory, route);
            makeCircuitBreakerFilter(routeFilters, circuitBreakerFilterFactory, route);
            makeRateLimiterFilter(routeFilters, rateLimiterFilterFactory, route, rateLimiter);

            routesBuilder.route(
                route.id(),
                routeSpec -> buildRoute(route, routeSpec, routeFilters)
            );
        }
        return routesBuilder.build();
    }

    private static Buildable<Route> buildRoute(
        ApiRoute route,
        PredicateSpec routeSpec,
        List<GatewayFilter> filters
    ) {
        return routeSpec
            .path(route.prefix() + "/**")
            .filters(filterSpec -> filterSpec.filters(filters)
            .rewritePath(
                route.prefix() + "/(?<segment>.*)",
                "/api/${segment}"
            ))
            .uri(route.uri());
    }

    private static void makeRateLimiterFilter(
        List<GatewayFilter> filters,
        RequestRateLimiterGatewayFilterFactory rateLimiterFilterFactory,
        ApiRoute route,
        RedisRateLimiter rateLimiter
    ) {
        var rateLimiterFilter = rateLimiterFilterFactory.apply(c -> {
            var redisRateLimiterConfig = new RedisRateLimiter.Config()
                .setReplenishRate(RATE_LIMITER_REPLENISH_RATE)
                .setBurstCapacity(RATE_LIMITER_BURST_CAPACITY);

            rateLimiter.getConfig().put(route.id(), redisRateLimiterConfig);
            c.setRouteId(route.id());
            c.setRateLimiter(rateLimiter);
        });
        filters.add(rateLimiterFilter);
    }

    private static void makeCircuitBreakerFilter(
        List<GatewayFilter> filters,
        SpringCloudCircuitBreakerResilience4JFilterFactory circuitBreakerFilterFactory,
        ApiRoute route
    ) {
        var circuitBreakerFilter = circuitBreakerFilterFactory.apply(c -> {
            c.setRouteId(route.id());
            c.setName(route.id());
            c.setStatusCodes(Set.of(HttpStatus.SERVICE_UNAVAILABLE.name()));
        });
        filters.add(circuitBreakerFilter);
    }

    private static void makeRetryFilter(
        List<GatewayFilter> filters,
        RetryGatewayFilterFactory retryFilterFactory,
        ApiRoute route
    ) {
        var retryFilter = retryFilterFactory.apply(c -> {
            c.setRouteId(route.id());
            c.setStatuses(TRANSIENT_HTTP_STATUSES);
            c.setMethods(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE);
            c.setBackoff(Duration.ofMillis(100), Duration.ofMillis(2000), 2, true);
        });
        filters.add(retryFilter);
    }
}

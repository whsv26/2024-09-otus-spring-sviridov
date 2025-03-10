package ru.demo.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.demo.controller.RequestIdFilter;

@Configuration
@EnableConfigurationProperties(ApplConfigProperties.class)
@EnableDiscoveryClient
public class ApiConfig {

    @Bean
    public RequestIdFilter requestIdFilter() {
        return new RequestIdFilter();
    }

    @Bean
    RouteLocator gateway(
        RouteLocatorBuilder routeLocatorBuilder,
        ApplConfigProperties applConfigProperties,
        RequestIdFilter filter
    ) {
        var routesBuilder = routeLocatorBuilder.routes();
        for (var route : applConfigProperties.getApiRoutes()) {
            routesBuilder.route(
                route.id(),
                routeSpec -> buildRoute(route, routeSpec, filter)
            );
        }
        return routesBuilder.build();
    }

    private static Buildable<Route> buildRoute(
        ApiRoute route,
        PredicateSpec routeSpec,
        RequestIdFilter requestIdFilter
    ) {
        return routeSpec
            .path(String.format("/%s/**", route.from()))
            .filters(filterSpec -> filterSpec.filters(requestIdFilter)
            .rewritePath(String.format("/%s/(?<segment>.*)", route.from()), "/${segment}"))
            .uri(String.format("%s/@", route.to()));
    }
}

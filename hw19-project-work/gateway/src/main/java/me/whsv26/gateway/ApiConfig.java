package me.whsv26.gateway;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(AppProps.class)
public class ApiConfig {

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
        List<GatewayFilter> filters
    ) {
        System.out.println("akdlkjasdlajs"); // TODO
        System.out.println(applConfigProperties.getApiRoutes()); // TODO

        var routesBuilder = routeLocatorBuilder.routes();
        for (var route : applConfigProperties.getApiRoutes()) {
            routesBuilder.route(
                route.id(),
                routeSpec -> buildRoute(route, routeSpec, filters)
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


}

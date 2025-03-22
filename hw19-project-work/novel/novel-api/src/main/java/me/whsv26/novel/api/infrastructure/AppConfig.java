package me.whsv26.novel.api.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.whsv26.libs.auth.CurrentUserProvider;
import me.whsv26.libs.auth.PreAuthenticationFilter;
import me.whsv26.libs.auth.SecurityContextHolderCurrentUserProvider;
import me.whsv26.libs.idempotency.IdempotencyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Clock;

@Configuration
public class AppConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    CurrentUserProvider currentUserProvider() {
        return new SecurityContextHolderCurrentUserProvider();
    }

    @Bean
    FilterRegistrationBean<PreAuthenticationFilter> preAuthenticationFilterRegistration() {
        var filter = new PreAuthenticationFilter();
        var registration = new FilterRegistrationBean<PreAuthenticationFilter>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }

    @Bean
    FilterRegistrationBean<IdempotencyFilter> idempotencyFilterRegistration(
        StringRedisTemplate redisTemplate,
        RedisProps redisProps,
        ObjectMapper objectMapper
    ) {
        var filter = new IdempotencyFilter(
            redisTemplate,
            objectMapper,
            redisProps.idempotencyProcessingTtl(),
            redisProps.idempotencyCacheTtl()
        );

        var registration = new FilterRegistrationBean<IdempotencyFilter>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }
}

package me.whsv26.novel.api.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "application.redis")
public record RedisProps(
    Duration idempotencyProcessingTtl,
    Duration idempotencyCacheTtl
) {}

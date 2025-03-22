package me.whsv26.novel.api.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "application.redis")
public record RedisProps(
    Duration idempotencyProcessingTtl,
    Duration idempotencyCacheTtl
) {}

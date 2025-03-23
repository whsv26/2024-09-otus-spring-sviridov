package me.whsv26.user.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    public static final String CACHE_PUBLIC_TOKENS = "publicTokens";

    public static final String CACHE_USERS = "users";

    @Bean
    public CacheManager cacheManager() {

        var cacheManager = new SimpleCacheManager();

        var publicTokensCache = new CaffeineCache(CACHE_PUBLIC_TOKENS,
            Caffeine.newBuilder().expireAfterWrite(24, TimeUnit.HOURS).build());

        var usersCache = new CaffeineCache(CACHE_USERS,
            Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build());

        cacheManager.setCaches(List.of(
            publicTokensCache,
            usersCache
        ));

        return cacheManager;
    }
}

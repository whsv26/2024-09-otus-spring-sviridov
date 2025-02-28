package ru.otus.hw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

@Data
@ConfigurationProperties(prefix = "app")
public final class AppProperties implements AuthConfig {

    private final int tokenTtl;

    private final URL tokenIssuer;

    private final String keyId;
}

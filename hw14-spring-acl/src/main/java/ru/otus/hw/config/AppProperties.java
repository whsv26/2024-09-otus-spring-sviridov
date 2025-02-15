package ru.otus.hw.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

@Value
@ConfigurationProperties(prefix = "app")
public class AppProperties implements AuthConfig {
    int tokenTtl;
    URL tokenIssuer;
    String keyId;
}

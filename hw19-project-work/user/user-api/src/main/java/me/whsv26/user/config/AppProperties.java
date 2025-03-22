package me.whsv26.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

@Data
@ConfigurationProperties(prefix = "application")
public final class AppProperties implements AuthConfig {

    private final int tokenTtl;

    private final URL tokenIssuer;

    private final String keyId;

    private final String publicKey;

    private final String privateKey;
}

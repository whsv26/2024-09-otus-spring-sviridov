package ru.otus.hw.infrastructure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.otus.hw.application.AuthConfig;

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

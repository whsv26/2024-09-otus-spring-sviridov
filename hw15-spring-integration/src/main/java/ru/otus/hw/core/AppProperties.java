package ru.otus.hw.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.smtp")
public final class AppProperties implements SmtpConfig {

    private final String host;

    private final Integer port;

    private final String username;

    private final String password;
}

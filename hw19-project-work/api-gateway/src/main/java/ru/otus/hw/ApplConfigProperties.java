package ru.otus.hw;

import java.util.List;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@Data
public class ApplConfigProperties {

    private final String authHost;

    private final List<ApiRoute> apiRoutes;

    private final String publicKey;
}

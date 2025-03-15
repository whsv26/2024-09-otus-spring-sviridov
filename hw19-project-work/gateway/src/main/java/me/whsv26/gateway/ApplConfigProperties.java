package me.whsv26.gateway;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "application")
@Data
public class ApplConfigProperties {

    private final String authHost;

    private final List<ApiRoute> apiRoutes;

    private final String publicKey;
}

package ru.otus.hw.config;

import java.util.List;

import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "application")
@ToString
@Getter
public class ApplConfigProperties {

    private final String authHost;

    private final List<ApiRoute> apiRoutes;

    @ConstructorBinding
    public ApplConfigProperties(String authHost, List<ApiRoute> apiRoutes) {
        this.authHost = authHost;
        this.apiRoutes = apiRoutes;
    }
}

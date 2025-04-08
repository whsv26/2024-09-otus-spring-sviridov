package me.whsv26.search.indexer;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

@ConfigurationProperties(prefix = "application.rest")
public record RestProps(UserApiProps userApi) {

    public record UserApiProps(URL url) {}
}

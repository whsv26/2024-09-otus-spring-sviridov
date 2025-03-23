package me.whsv26.search.indexer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.elasticsearch")
public record ElasticsearchProps(
    String index,
    String host,
    int port
) {}

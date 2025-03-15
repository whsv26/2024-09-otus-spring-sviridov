package me.whsv26.search.indexer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${application.elasticsearch.index}")
    private String indexName;

    @Bean
    public String indexName() {
        return indexName;
    }
}

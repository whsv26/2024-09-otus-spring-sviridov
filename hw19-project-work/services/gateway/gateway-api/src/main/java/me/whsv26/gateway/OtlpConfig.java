package me.whsv26.gateway;

import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtlpConfig {

    @Bean
    OtlpHttpSpanExporter otlpHttpSpanExporter(@Value("${management.otlp.tracing.endpoint}") String url) {
        return OtlpHttpSpanExporter.builder()
            .setEndpoint(url)
            .build();
    }
}

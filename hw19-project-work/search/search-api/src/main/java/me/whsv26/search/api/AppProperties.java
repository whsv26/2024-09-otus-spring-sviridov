package me.whsv26.search.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application")
public final class AppProperties {

}

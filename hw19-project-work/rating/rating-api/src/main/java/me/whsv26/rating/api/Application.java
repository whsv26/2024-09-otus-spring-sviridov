package me.whsv26.rating.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import me.whsv26.novel.api.infrastructure.AppProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;

//@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

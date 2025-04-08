package me.whsv26.novel.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ConfigurationPropertiesScan
@EnableMongoRepositories(basePackages = {"me.whsv26.novel.api", "me.whsv26.libs.outbox"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

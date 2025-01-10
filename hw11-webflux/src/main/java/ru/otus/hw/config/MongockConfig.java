package ru.otus.hw.config;

import com.mongodb.reactivestreams.client.MongoClient;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongockConfig {

    @Bean
    public MongoReactiveDriver reactiveConnectionDriver(MongoClient mongoClient, MongoProperties mongoProperties) {
        var driver = MongoReactiveDriver.withDefaultLock(mongoClient, mongoProperties.getDatabase());
        driver.disableTransaction();
        return driver;
    }
}

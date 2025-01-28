package ru.otus.hw.config;

import io.mongock.driver.mongodb.springdata.v4.SpringDataMongoV4Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongockConfig {

//    @Bean
//    public SpringDataMongoV4Driver connectionDriverWithoutTransactions(MongoTemplate mongoTemplate) {
//        var driver = SpringDataMongoV4Driver.withDefaultLock(mongoTemplate);
//        driver.disableTransaction();
//        return driver;
//    }
}

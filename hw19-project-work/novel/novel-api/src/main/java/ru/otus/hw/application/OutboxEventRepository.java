package ru.otus.hw.application;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OutboxEventRepository extends MongoRepository<OutboxEvent, String> {
}

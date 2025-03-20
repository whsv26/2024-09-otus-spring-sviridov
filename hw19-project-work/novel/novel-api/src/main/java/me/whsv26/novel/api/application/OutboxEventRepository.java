package me.whsv26.novel.api.application;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OutboxEventRepository extends MongoRepository<OutboxMessage, String> {
}

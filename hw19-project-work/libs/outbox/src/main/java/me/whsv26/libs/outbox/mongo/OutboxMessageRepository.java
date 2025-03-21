package me.whsv26.libs.outbox.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OutboxMessageRepository extends MongoRepository<OutboxMessage, String> {
}

package me.whsv26.search.indexer;

import lombok.RequiredArgsConstructor;
import me.whsv26.novel.model.NovelEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NovelEventConsumer {

    private final ElasticsearchOperations elasticsearchOperations;

    private final UserClient userClient;

    @Value("${application.elasticsearch.index}")
    private final String indexName;

    @KafkaListener(
        topics = "${application.kafka.novel.topic}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMessage(@Payload List<NovelEvent> events) {
        var operations = events.stream()
            .map(this::buildUpdateQuery)
            .toList();

        elasticsearchOperations.bulkUpdate(operations, IndexCoordinates.of(indexName));
    }

    private UpdateQuery buildUpdateQuery(NovelEvent e) {
        var authorName = userClient.getUsername(e.authorId());
        var novel = Document.create();
        novel.put("title", e.title());
        novel.put("synopsis", e.synopsis());
        novel.put("authorId", e.authorId());
        novel.put("authorName", authorName);
        novel.put("genres", e.genres());
        novel.put("tags", e.tags());

        return UpdateQuery.builder(e.id())
            .withUpsert(novel)
            .withRefreshPolicy(RefreshPolicy.NONE)
            .build();
    }
}
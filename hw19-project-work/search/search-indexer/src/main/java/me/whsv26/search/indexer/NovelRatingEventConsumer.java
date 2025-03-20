package me.whsv26.search.indexer;

import lombok.RequiredArgsConstructor;
import me.whsv26.rating.model.NovelRatingEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NovelRatingEventConsumer {

    private final ElasticsearchOperations elasticsearchOperations;

    private final ElasticsearchProps props;

    @KafkaListener(
        topics = "${application.kafka.consumer.rating-event.topic}",
        containerFactory = "kafkaListenerContainerFactoryNovelRatingEvent"
    )
    public void consumeMessage(@Payload List<NovelRatingEvent> events) {
        var operations = events.stream()
            .collect(Collectors.toMap(
                NovelRatingEvent::novelId,
                NovelRatingEvent::rating
            ))
            .entrySet()
            .stream()
            .map(entry -> buildUpdateQuery(entry.getKey(), entry.getValue()))
            .toList();

        elasticsearchOperations.bulkUpdate(operations, IndexCoordinates.of(props.index()));
    }

    private UpdateQuery buildUpdateQuery(String novelId, float rating) {
        var novel = Document.create();
        novel.put("rating", rating);

        return UpdateQuery.builder(novelId)
            .withDocument(novel)
            .withDocAsUpsert(true)
            .withRefreshPolicy(RefreshPolicy.NONE)
            .build();
    }
}
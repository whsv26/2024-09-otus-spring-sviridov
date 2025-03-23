package me.whsv26.search.indexer;

import lombok.RequiredArgsConstructor;
import me.whsv26.novel.model.NovelCreatedEvent;
import me.whsv26.novel.model.NovelDeletedEvent;
import me.whsv26.novel.model.NovelEvent;
import me.whsv26.novel.model.NovelUpdatedEvent;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static me.whsv26.search.indexer.NovelFields.FIELD_AUTHOR_ID;
import static me.whsv26.search.indexer.NovelFields.FIELD_AUTHOR_NAME;
import static me.whsv26.search.indexer.NovelFields.FIELD_GENRES;
import static me.whsv26.search.indexer.NovelFields.FIELD_ID;
import static me.whsv26.search.indexer.NovelFields.FIELD_SYNOPSIS;
import static me.whsv26.search.indexer.NovelFields.FIELD_TAGS;
import static me.whsv26.search.indexer.NovelFields.FIELD_TITLE;

@Component
@RequiredArgsConstructor
public class NovelEventConsumer {

    private final ElasticsearchOperations elasticsearchOperations;

    private final UserClient userClient;

    private final ElasticsearchProps props;

    @KafkaListener(
        topics = "${application.kafka.consumer.novel-event.topic}",
        containerFactory = "kafkaListenerContainerFactoryNovelEvent"
    )
    public void consumeMessage(@Payload List<NovelEvent> events) {
        var indexCoordinates = IndexCoordinates.of(props.index());
        events.stream()
            .collect(Collectors.groupingBy(NovelEvent::getType))
            .forEach((type, eventsOfType) -> {
                switch (type) {
                    case CREATED -> {
                        var queries = events.stream().map(e -> buildCreateQuery((NovelCreatedEvent) e)).toList();
                        elasticsearchOperations.bulkUpdate(queries, indexCoordinates);
                    }
                    case UPDATED -> {
                        var queries = events.stream().map(e -> buildUpdateQuery((NovelUpdatedEvent) e)).toList();
                        elasticsearchOperations.bulkUpdate(queries, indexCoordinates);
                    }
                    case DELETED -> {
                        var query = buildDeleteQuery(events.stream().map(e -> (NovelDeletedEvent) e).toList());
                        elasticsearchOperations.delete(query, Object.class, indexCoordinates);
                    }
                }
            });
    }

    private UpdateQuery buildCreateQuery(NovelCreatedEvent e) {
        var authorName = userClient.getUsername(e.authorId()).orElse("");
        var novel = Document.create();
        novel.put(FIELD_TITLE, e.title());
        novel.put(FIELD_SYNOPSIS, e.synopsis());
        novel.put(FIELD_AUTHOR_ID, e.authorId());
        novel.put(FIELD_AUTHOR_NAME, authorName);
        novel.put(FIELD_GENRES, e.genres());
        novel.put(FIELD_TAGS, e.tags());

        return UpdateQuery.builder(e.novelId())
            .withDocument(novel)
            .withDocAsUpsert(true)
            .withRefreshPolicy(RefreshPolicy.NONE)
            .build();
    }

    private UpdateQuery buildUpdateQuery(NovelUpdatedEvent e) {
        var novel = Document.create();
        novel.put(FIELD_TITLE, e.title());
        novel.put(FIELD_SYNOPSIS, e.synopsis());
        novel.put(FIELD_GENRES, e.genres());
        novel.put(FIELD_TAGS, e.tags());

        return UpdateQuery.builder(e.novelId())
            .withDocument(novel)
            .withDocAsUpsert(true)
            .withRefreshPolicy(RefreshPolicy.NONE)
            .build();
    }

    private static DeleteQuery buildDeleteQuery(List<NovelDeletedEvent> events) {
        var novelIds = events.stream().map(NovelDeletedEvent::novelId).toList();
        var criteria = Criteria.where(FIELD_ID).in(novelIds);
        var query = CriteriaQuery.builder(criteria).build();
        return DeleteQuery.builder(query).build();
    }
}
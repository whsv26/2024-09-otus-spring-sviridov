package me.whsv26.novel.api.domain.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.whsv26.novel.api.domain.valueobject.AuthorId;
import me.whsv26.novel.api.domain.valueobject.GenreId;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import me.whsv26.novel.model.NovelCreatedEvent;
import me.whsv26.novel.model.NovelDeletedEvent;
import me.whsv26.novel.model.NovelUpdatedEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document
public class Novel extends AbstractAggregateRoot<Novel> {

    @Id
    @Setter(AccessLevel.NONE)
    private NovelId id;

    private String title;

    private String synopsis;

    private AuthorId authorId;

    private List<GenreId> genres;

    private List<String> tags;

    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @Version
    @Setter(AccessLevel.NONE)
    private Long version;

    public static Novel create(
        NovelId id,
        String title,
        String synopsis,
        AuthorId authorId,
        List<GenreId> genres,
        List<String> tags,
        Clock clock
    ) {
        var novel = new Novel();
        novel.id = id;
        novel.title = title;
        novel.synopsis = synopsis;
        novel.authorId = authorId;
        novel.genres = genres;
        novel.tags = tags;
        novel.createdAt = clock.instant().atZone(clock.getZone()).toLocalDateTime();

        novel.registerEvent(new NovelCreatedEvent(
            UUID.randomUUID().toString(),
            novel.getId().value(),
            novel.getTitle(),
            novel.getSynopsis(),
            novel.getAuthorId().value(),
            novel.getGenres().stream().map(GenreId::value).toList(),
            novel.getTags(),
            clock.instant().atZone(clock.getZone()).toLocalDateTime()
        ));

        return novel;
    }

    public void update(
        String title,
        String synopsis,
        List<GenreId> genres,
        List<String> tags,
        Clock clock
    ) {
        boolean isChanged = false;

        if (!Objects.equals(this.title, title)) {
            this.title = title;
            isChanged = true;
        }

        if (!Objects.equals(this.synopsis, synopsis)) {
            this.synopsis = synopsis;
            isChanged = true;
        }

        if (!Objects.equals(this.genres, genres)) {
            this.genres = genres;
            isChanged = true;
        }

        if (!Objects.equals(this.tags, tags)) {
            this.tags = tags;
            isChanged = true;
        }

        if (isChanged) {
            registerEvent(new NovelUpdatedEvent(
                UUID.randomUUID().toString(),
                this.id.value(),
                this.title,
                this.synopsis,
                this.genres.stream().map(GenreId::value).toList(),
                this.tags,
                clock.instant().atZone(clock.getZone()).toLocalDateTime()
            ));
        }
    }

    public void delete(Clock clock) {
        registerEvent(new NovelDeletedEvent(
            UUID.randomUUID().toString(),
            this.id.value(),
            clock.instant().atZone(clock.getZone()).toLocalDateTime()
        ));
    }
}

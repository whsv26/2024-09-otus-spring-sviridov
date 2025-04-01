package me.whsv26.novel.api.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.whsv26.novel.api.domain.valueobject.AuthorId;
import me.whsv26.novel.api.domain.valueobject.CreateNovelDetails;
import me.whsv26.novel.api.domain.valueobject.GenreId;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import me.whsv26.novel.api.domain.valueobject.UpdateNovelDetails;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document
@Builder(toBuilder = true)
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

    public static Novel create(CreateNovelDetails details, Clock clock) {
        var novel = new Novel();
        novel.id = details.id();
        novel.title = details.title();
        novel.synopsis = details.synopsis();
        novel.authorId = details.authorId();
        novel.genres = details.genres();
        novel.tags = details.tags();
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

    public void update(UpdateNovelDetails details, Clock clock) {
        var updateResults = List.of(
            updateTitle(details),
            updateSynopsis(details),
            updateGenres(details),
            updateTags(details)
        );

        var isChanged = updateResults.stream()
            .anyMatch(result -> result.equals(Boolean.TRUE));

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

    private boolean updateTags(UpdateNovelDetails details) {
        if (!Objects.equals(this.tags, details.tags())) {
            this.tags = details.tags();
            return true;
        }
        return false;
    }

    private boolean updateGenres(UpdateNovelDetails details) {
        if (!Objects.equals(this.genres, details.genres())) {
            this.genres = details.genres();
            return true;
        }
        return false;
    }

    private boolean updateSynopsis(UpdateNovelDetails details) {
        if (!Objects.equals(this.synopsis, details.synopsis())) {
            this.synopsis = details.synopsis();
            return true;
        }
        return false;
    }

    private boolean updateTitle(UpdateNovelDetails details) {
        if (!Objects.equals(this.title, details.title())) {
            this.title = details.title();
            return true;
        }
        return false;
    }

    public void delete(Clock clock) {
        registerEvent(new NovelDeletedEvent(
            UUID.randomUUID().toString(),
            this.id.value(),
            clock.instant().atZone(clock.getZone()).toLocalDateTime()
        ));
    }
}

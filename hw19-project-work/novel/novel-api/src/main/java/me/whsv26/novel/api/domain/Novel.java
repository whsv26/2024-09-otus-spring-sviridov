package me.whsv26.novel.api.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document
public class Novel {

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

    public Novel(
        NovelId id,
        String title,
        String synopsis,
        AuthorId authorId,
        List<GenreId> genres,
        List<String> tags,
        Clock clock
    ) {
        this.id = id;
        this.title = title;
        this.synopsis = synopsis;
        this.authorId = authorId;
        this.genres = genres;
        this.tags = tags;
        this.createdAt = clock.instant().atZone(clock.getZone()).toLocalDateTime();
    }
}

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

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document
public class Chapter {

    @Id
    @Setter(AccessLevel.NONE)
    private ChapterId id;

    private NovelId novelId;

    private String title;

    private String content;

    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @Version
    @Setter(AccessLevel.NONE)
    private Long version;

    public Chapter(ChapterId id, NovelId novelId, String title, String content, Clock clock) {
        this.id = id;
        this.novelId = novelId;
        this.title = title;
        this.content = content;
        this.createdAt = clock.instant().atZone(clock.getZone()).toLocalDateTime();
    }
}

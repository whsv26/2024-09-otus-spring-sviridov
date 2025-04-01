package me.whsv26.novel.api.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.whsv26.novel.api.domain.valueobject.ChapterId;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Clock;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document
@Builder(toBuilder = true)
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

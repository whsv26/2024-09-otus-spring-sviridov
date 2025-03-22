package me.whsv26.novel.api.infrastructure;

import lombok.RequiredArgsConstructor;
import me.whsv26.novel.api.application.port.in.ChapterUseCases;
import me.whsv26.novel.api.domain.Novel;
import me.whsv26.novel.api.domain.NovelId;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NovelCascadeDeleteListener extends AbstractMongoEventListener<Novel> {

    private final ChapterUseCases chapterService;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Novel> event) {
        var source = event.getSource();
        var novelId = source.get("_id", String.class);
        chapterService.deleteByNovelId(new NovelId(novelId));
    }
}

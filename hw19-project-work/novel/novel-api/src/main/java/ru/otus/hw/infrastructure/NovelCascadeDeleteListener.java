package ru.otus.hw.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.application.ChapterRepository;
import ru.otus.hw.domain.Novel;
import ru.otus.hw.domain.NovelId;

@Component
@RequiredArgsConstructor
public class NovelCascadeDeleteListener extends AbstractMongoEventListener<Novel> {

    private final ChapterRepository chapterRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Novel> event) {
        var source = event.getSource();
        var novelId = source.getObjectId("_id").toString();
        chapterRepository.deleteByNovelId(new NovelId(novelId));
    }
}

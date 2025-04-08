package me.whsv26.novel.api.infrastructure;

import lombok.RequiredArgsConstructor;
import me.whsv26.novel.api.application.port.in.DeleteNovelChaptersUseCase;
import me.whsv26.novel.api.domain.entity.Novel;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NovelCascadeDeleteListener extends AbstractMongoEventListener<Novel> {

    private final DeleteNovelChaptersUseCase useCase;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Novel> event) {
        var source = event.getSource();
        var novelId = source.get("_id", String.class);
        useCase.deleteByNovelId(new NovelId(novelId));
    }
}

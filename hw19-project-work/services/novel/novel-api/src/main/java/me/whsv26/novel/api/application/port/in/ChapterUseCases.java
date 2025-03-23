package me.whsv26.novel.api.application.port.in;

public interface ChapterUseCases extends
    CreateChapterUseCase,
    UpdateChapterUseCase,
    FindChapterByNovelIdUseCase,
    FindChapterByIdUseCase,
    DeleteChapterUseCase,
    DeleteNovelChaptersUseCase {

}

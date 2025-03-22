package me.whsv26.novel.api.application.port.in;

public interface NovelUseCases extends
    FindNovelByAuthorIdUseCase,
    FindNovelByIdUseCase,
    CreateNovelUseCase,
    UpdateNovelUseCase,
    DeleteNovelUseCase {

}

package me.whsv26.rating.api;

import me.whsv26.rating.model.NovelRatingCommand;

public interface NovelRatingCommandSender {

    void send(NovelRatingCommand command);
}

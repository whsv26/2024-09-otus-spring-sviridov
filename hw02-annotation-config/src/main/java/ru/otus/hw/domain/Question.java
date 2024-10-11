package ru.otus.hw.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record Question(String text, List<Answer> answers) {
    public boolean isAnsweredCorrectly(Set<Answer> pickedAnswers) {
        var correctAnswers = this.answers().stream()
            .filter(Answer::isCorrect)
            .collect(Collectors.toSet());

        return pickedAnswers.equals(correctAnswers);
    }
}

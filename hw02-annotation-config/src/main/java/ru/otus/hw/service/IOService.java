package ru.otus.hw.service;

public interface IOService {
    void printLine(String s);

    int readIntForRange(int min, int max, String errorMessage);

    String readString();

    default String readStringWithPrompt(String prompt) {
        printLine(prompt);
        return readString();
    }

    default void printFormattedLine(String s, Object... args) {
        printLine(s.formatted(args));
    }

    default int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        printLine(prompt);
        return readIntForRange(min, max, errorMessage);
    }
}

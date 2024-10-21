package ru.otus.hw.service;

public interface LocalizedIOService extends LocalizedMessagesService, IOService {

    default void printLineLocalized(String code) {
        printLine(getMessage(code));
    }

    default void printFormattedLineLocalized(String code, Object ...args) {
        printLine(getMessage(code, args));
    }

    default String readStringWithPromptLocalized(String promptCode) {
        var prompt = getMessage(promptCode);
        return readStringWithPrompt(prompt);
    }

    default int readIntForRangeLocalized(int min, int max, String errorMessageCode) {
        var errorMessage = getMessage(errorMessageCode);
        return readIntForRange(min, max, errorMessage);
    }

    default int readIntForRangeWithPromptLocalized(int min, int max, String promptCode, String errorMessageCode) {
        printLine(getMessage(promptCode));
        return readIntForRangeLocalized(min, max, errorMessageCode);
    }
}

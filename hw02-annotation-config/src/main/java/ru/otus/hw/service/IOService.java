package ru.otus.hw.service;

public interface IOService {
    void printLine(String s);

    void printFormattedLine(String s, Object ...args);

    String readString();

    default String readStringWithPrompt(String prompt) {
        printLine(prompt);
        return readString();
    }

    default <T> T read(Reader<T> reader) {
        return reader.read(readString());
    }

    default <T> T readWithPrompt(Reader<T> reader, String prompt) {
        printLine(prompt);
        return read(reader);
    }

}

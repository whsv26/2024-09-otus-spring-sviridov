package ru.otus.hw.service;

import java.util.List;

public interface IOService {
    
    void printLine(String s);

    String readStringWithPrompt(String prompt);

    String selectStringWithPrompt(String prompt, List<String> items);
}

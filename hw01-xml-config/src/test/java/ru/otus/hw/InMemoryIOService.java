package ru.otus.hw;

import ru.otus.hw.service.IOService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InMemoryIOService implements IOService {

    private final List<String> linesLog = new LinkedList<>();

    @Override
    public void printLine(String s) {
        linesLog.add(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        linesLog.add(s.formatted(args));
    }

    public List<String> getLinesLog() {
        return Collections.unmodifiableList(linesLog);
    }
}

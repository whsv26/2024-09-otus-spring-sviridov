package ru.otu.hw;

import lombok.Data;
import ru.otus.hw.service.IOService;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Data
public class SimpleIOService implements IOService {

    private List<String> writeSink = new LinkedList<>();

    private Iterator<String> readSource = Collections.emptyIterator();

    @Override
    public void printLine(String s) {
        if (!s.isEmpty()) {
            writeSink.add(s);
        }
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        if (!s.isEmpty()) {
            writeSink.add(s.formatted(args));
        }
    }

    @Override
    public String readString() {
        return readSource.next();
    }
}

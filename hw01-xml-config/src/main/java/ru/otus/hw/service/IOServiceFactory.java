package ru.otus.hw.service;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class IOServiceFactory {
    public IOService createConsoleIO() {
        var printStream = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        return new StreamsIOService(printStream);
    }
}

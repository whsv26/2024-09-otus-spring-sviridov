package ru.otus.hw.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

@Service
@Primary
public class StreamsIOService implements IOService {

    private static final int MAX_ATTEMPTS = 10;

    private final PrintStream printStream;

    private final Scanner scanner;

    public StreamsIOService(@Value("#{T(System).out}") PrintStream printStream,
                            @Value("#{T(System).in}") InputStream inputStream) {

        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            var stringValue = scanner.nextLine();
            var maybeIntValue = parseIntForRange(stringValue, min, max);

            if (maybeIntValue.isPresent()) {
                return maybeIntValue.get();
            }

            printLine(errorMessage);
        }
        throw new IllegalArgumentException("Error during reading int value");
    }

    private static Optional<Integer> parseIntForRange(String stringValue, int min, int max) {
        try {
            int intValue = Integer.parseInt(stringValue);
            return intValue < min || intValue > max
                ? Optional.empty()
                : Optional.of(intValue);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}

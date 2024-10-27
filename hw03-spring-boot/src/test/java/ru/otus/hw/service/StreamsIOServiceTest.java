package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.service.StreamsIOService;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StreamsIOServiceTest {

    @Mock
    private PrintStream printStream;

    @DisplayName("Should try to read int for range no more than ten times")
    @Test
    public void shouldTryToReadIntForRangeNoMoreThanTenTimes() {
        var inputString = IntStream.range(1, 11)
            .mapToObj(String::valueOf)
            .collect(Collectors.joining("\n"));

        var inputStream = new ByteArrayInputStream(inputString.getBytes());
        var ioService = new StreamsIOService(printStream, inputStream);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .describedAs("Error during reading int value")
            .isThrownBy(() -> ioService.readIntForRange(0, 0, "error"));

        verify(printStream, times(10)).println("error");
    }

}

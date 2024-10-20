package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LocalizedIOServiceImpl implements LocalizedIOService {

    private final LocalizedMessagesService localizedMessagesService;

    private final IOService ioService;

    @Override
    public void printLine(String s) {
        ioService.printLine(s);
    }

    @Override
    public String readString() {
        return ioService.readString();
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        return ioService.readIntForRange(min, max, errorMessage);
    }

    @Override
    public String getMessage(String code, Object... args) {
        return localizedMessagesService.getMessage(code, args);
    }
}

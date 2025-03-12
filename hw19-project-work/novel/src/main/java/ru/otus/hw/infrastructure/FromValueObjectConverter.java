package ru.otus.hw.infrastructure;

import org.springframework.core.convert.converter.Converter;
import ru.otus.hw.domain.ValueObject;

public class FromValueObjectConverter implements Converter<ValueObject<String>, String> {

    @Override
    public String convert(ValueObject<String> source) {
        return source.value();
    }
}

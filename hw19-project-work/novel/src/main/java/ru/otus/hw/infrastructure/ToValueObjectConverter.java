package ru.otus.hw.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import ru.otus.hw.domain.ValueObject;

import java.util.function.Function;

@AllArgsConstructor
public class ToValueObjectConverter<T extends ValueObject<String>> implements Converter<String, T> {

    private final Function<String, T> convert;

    @Override
    public T convert(String source) {
        return convert.apply(source);
    }
}

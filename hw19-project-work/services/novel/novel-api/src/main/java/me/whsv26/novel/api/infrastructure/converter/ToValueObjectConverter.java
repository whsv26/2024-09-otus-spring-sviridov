package me.whsv26.novel.api.infrastructure.converter;

import lombok.AllArgsConstructor;
import me.whsv26.novel.api.domain.valueobject.ValueObject;
import org.springframework.core.convert.converter.Converter;

import java.util.function.Function;

@AllArgsConstructor
public class ToValueObjectConverter<T extends ValueObject<String>> implements Converter<String, T> {

    private final Function<String, T> convert;

    @Override
    public T convert(String source) {
        return convert.apply(source);
    }
}
